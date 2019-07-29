package isop.test


import isop.AppHostBuilder
import isop.MissingArgumentException
import isop.TypeConversionFailedException
import isop.command_line.ControllerRecognizer
import isop.command_line.parse.*
import isop.domain.Argument
import isop.test.fake_controllers.EnumerableController
import isop.test.fake_controllers.MyController
import isop.test.fake_controllers.MyOptionalController
import isop.test.fake_controllers.WithIndexController
import isop.test.helpers.Counter
import isop.test.testData.SingleIntAction
import org.junit.Test

import java.util.ArrayList
import java.util.Arrays
import java.util.function.Function
import java.util.stream.Stream

import org.junit.Assert.*
import java.lang.Boolean
import java.util.function.Consumer

/**
 *
 * @author mathieu
 */
class ArgumentParserTest : Base() {

    @Test
    fun Recognizes_short_form() {
        val arguments = AppHostBuilder().parameter("&argument").parse(arrayOf("-a")).recognizedArguments
        assertEquals(1, arguments.size.toLong())
        val arg1 = arguments.iterator().next()
        assertEquals("argument", arg1.argument.name)
    }

    @Test
    fun Given_several_arguments_Then_the_correct_one_is_recognized() {
        val arguments = AppHostBuilder().parameter("&beta").parse(arrayOf("-a", "-b")).recognizedArguments
        assertEquals(1, arguments.size.toLong())
        val first = arguments.iterator().next()
        assertEquals("b", first.rawArgument)
    }

    @Test
    fun Recognizes_long_form() {
        val arguments = AppHostBuilder().parameter("beta").parse(arrayOf("-a", "--beta")).recognizedArguments
        assertEquals(1, arguments.size.toLong())
        val first = arguments.iterator().next()
        assertEquals("beta", first.rawArgument)
    }

    @Test
    fun It_can_parse_parameter_value() {
        val arguments = AppHostBuilder()
                .parameter("beta")
                .parse(arrayOf("-a", "--beta", "value"))
                .recognizedArguments
        assertEquals(1, arguments.size.toLong())
        val first = arguments.iterator().next()
        assertEquals("beta", first.rawArgument)
        assertEquals("value", first.value)
    }

    @Test
    fun It_can_parse_ordinal_parameters() {
        val o = OrdinalParameter.tryParse("#1first")
        assertNotNull(o)
    }

    @Test
    fun It_can_parse_ordinal_parameter_value() {
        val arguments = AppHostBuilder()
                .parameter("#0first")
                .parse(arrayOf("first"))
                .recognizedArguments
        assertEquals(1, arguments.size.toLong())
        val first = arguments.iterator().next()
        assertEquals("first", first.rawArgument)
    }

    @Test
    fun It_can_parse_parameter_with_equals() {
        val arguments = AppHostBuilder().parameter("beta=").parse(arrayOf("-a", "--beta=test", "value")).recognizedArguments
        assertEquals(1, arguments.size.toLong())
        val first = arguments.iterator().next()
        assertEquals("beta", first.rawArgument)
        assertEquals("test", first.value)
    }

    @Test
    fun It_can_parse_parameter_alias() {
        val arguments = AppHostBuilder()
                .parameter("beta|b=")
                .parse(arrayOf("-a", "-b=test", "value"))
                .recognizedArguments
        assertEquals(1, arguments.size.toLong())
        val first = arguments.iterator().next()
        assertEquals("beta", first.argument.name)
        assertEquals("b", first.rawArgument)
        assertEquals("test", first.value)
    }

    @Test
    fun It_can_report_unrecognized_parameters() {
        val unRecognizedArguments = AppHostBuilder().parameter("beta").parse(arrayOf("-a", "value", "--beta")).unRecognizedArguments
        val expected = arrayOf(UnrecognizedArgument(0, "-a"), UnrecognizedArgument(1, "value"))
        assertArrayEquals(expected, unRecognizedArguments.toTypedArray())
    }

    @Test
    fun It_can_infer_ordinal_usage_of_named_parameters() {
        val arguments = AppHostBuilder()
                .parameter("beta|b=")
                .parameter("alpha|a=")
                .parse(arrayOf("test", "value"))
                .recognizedArguments
        assertArrayEquals(arrayOf("test", "value"),
                arguments.stream().map({ arg -> arg.value }).toArray())
    }

    @Test
    fun It_wont_report_matched_parameters() {
        val arguments = AppHostBuilder()
                .parameter("beta")
                .parse(arrayOf("--beta", "value"))
                .unRecognizedArguments
        assertEquals(0, arguments.size.toLong())
    }

    @Test(expected = MissingArgumentException::class)
    fun It_will_fail_if_argument_not_supplied_and_it_is_required() {
        AppHostBuilder().parameter("beta", true).parse(arrayOf("-a", "value"))
        fail()
    }

    @Test
    fun It_can_recognize_arguments() {
        val arguments = AppHostBuilder().parameter("alpha").parse(arrayOf("alpha")).recognizedArguments

        assertEquals(1, arguments.size.toLong())
        val arg1 = arguments.iterator().next()
        assertEquals("alpha", arg1.argument.name)
        assertEquals(null, arg1.value)
        assertEquals("alpha", arg1.rawArgument)
    }

    @Test
    fun It_can_parse_class_and_method_and_execute() {
        val count = Counter()

        val factory = Function<Class<*>,Any>{ c ->
            assertEquals(MyController::class.java, c)
            object : MyController() {

                override fun action(p: MyController.Param): String? {
                    count.next()
                    return null
                }
            }
        }
        val arguments = AppHostBuilder()
                .setFactory(factory)
                .recognizeClass(MyController::class.java)
                .parse(arrayOf("my", "action", "--param2", "value2", "--param3", "3", "--param1", "value1", "--param4", "3.4"))
        assertEquals(0, arguments.unRecognizedArguments.size.toLong())
        arguments.invoke().toArray()
        assertEquals(1, count.count.toLong())
    }

    @Test
    fun It_can_parse_class_and_method_and_execute_with_ordinal_syntax() {
        val count = Counter()
        val factory = Function<Class<*>,Any>{ t: Class<*> ->
            assertEquals(MyController::class.java, t)
            MyController()
                    .setOnAction { p1 -> (count.next() as Any).toString() } as Any
        }
        val arguments = AppHostBuilder()
                .recognizeClass(MyController::class.java)
                .setFactory(factory)
                .parse(arrayOf("My", "action", "value1", "value2", "3", "3.4"))

        assertEquals(0, arguments.unRecognizedArguments.size.toLong())
        arguments.invoke().toArray()
        assertEquals(1, count.count.toLong())
    }

    @Test
    fun It_can_parse_class_and_method_and_knows_whats_required() {
        val factory = Function<Class<*>,Any>{ t: Class<*> ->
            assertEquals(MyController::class.java, t)
            MyController().setOnAction { p1 -> "" } as Any
        }
        val build = AppHostBuilder()
                .recognizeClass(MyController::class.java)
                .setFactory(factory)

        val first = build.controllerRecognizers
                .values.stream()
                .findFirst().get().invoke()
        val recognizers = first.getRecognizers("action")
        assertArrayEquals(
                Base.dictionaryDescriptionToKv("[param1, true], [param2, true], [param3, true], [param4, true]", Function{ Boolean.parseBoolean(it) }).toTypedArray(),
                recognizers.stream().map { r -> Base.toSimpleEntry(r.name, r.required) }.toArray())
    }

    @Test
    fun It_can_parse_class_and_method_and_knows_whats_not_required() {
        val factory = Function<Class<*>,Any> { t: Class<*> ->
            assertEquals(MyController::class.java, t)
            MyOptionalController().setOnAction { p1 -> "" } as Any
        }
        val build = AppHostBuilder()
                .recognizeClass(MyOptionalController::class.java)
                .setFactory(factory)

        val first = build.controllerRecognizers
                .values.stream()
                .findFirst().get()
                .invoke()
        val recognizers = first.getRecognizers("action")
        assertArrayEquals(
                dictionaryDescriptionToKv("[param1, true], [param2, false], [param3, false], [param4, false]", Function<String, Any>{ Boolean.parseBoolean(it) }).toTypedArray(),
                recognizers.stream().map({ r -> toSimpleEntry(r.name, r.required) }).toArray())
    }

    //@Test TODO: Not implemented!
    fun It_can_parse_class_and_method_and_executes_default_with_the_default_values() {
        val parameters = ArrayList<MyOptionalController.Param>()
        val factory =Function<Class<*>,Any> { t: Class<*> ->
            assertEquals(MyOptionalController::class.java, t)
            MyOptionalController().setOnAction { p1 ->
                parameters.add(p1)
                ""
            } as Any
        }
        val arguments = AppHostBuilder()
                .recognizeClass(MyOptionalController::class.java)
                .setFactory(factory)
                .parse(arrayOf("MyOptional", "action", "--param1", "value1"))

        arguments.invoke().toArray()
        assertArrayEquals(arrayOf<Any?>("value1", null, null, 1), parameters[0].toArray())
    }

    //@Test TODO: Not implemented!
    fun It_can_parse_class_and_method_and_executes_default_with_the_default_values_when_using_ordinal_syntax() {
        val parameters = ArrayList<MyOptionalController.Param>()
        val factory =Function { t: Class<*> ->
            assertEquals(MyOptionalController::class.java, t)
            MyOptionalController().setOnAction { p1 ->
                parameters.add(p1)
                ""
            } as Any
        }
        val arguments = AppHostBuilder()
                .recognizeClass(MyOptionalController::class.java)
                .setFactory(factory)
                .parse(arrayOf("MyOptional", "action", "value1"))

        arguments.invoke().toArray()
        assertArrayEquals(arrayOf<Any?>("value1", null, null, 1), parameters[0].toArray())
    }


    @Test(expected = MissingArgumentException::class)
    fun It_can_parse_class_and_method_and_fail() {
        val builder = AppHostBuilder().recognizeClass(MyController::class.java)
        builder.parse(arrayOf("My", "action", "--param2", "value2", "--paramX", "3", "--param1", "value1", "--param4", "3.4"))
        fail()
    }

    @Test(expected = TypeConversionFailedException::class)
    fun It_can_parse_class_and_method_and_fail_because_of_type_conversion() {
        val builder = AppHostBuilder()
                .setFactory (Function { c -> SingleIntAction() })
                .recognizeClass(SingleIntAction::class.java)
        builder.parse(arrayOf("SingleIntAction", "action", "--param", "value"))
        fail()
    }

    @Test(expected = MissingArgumentException::class)
    fun It_can_parse_class_and_method_and_fail_because_no_arguments_given() {
        val builder = AppHostBuilder().recognizeClass(MyController::class.java)
        builder.parse(arrayOf("My", "action"))
        fail()
    }

    @Test
    fun It_can_parse_class_and_method_and_also_arguments_and_execute() {
        val count = Counter()
        val f = Function<Class<*>,Any>{ c ->
            assertEquals(MyController::class.java, c)
            object : MyController() {

                override fun action(p: MyController.Param): String? {
                    count.next()
                    return null
                }
            }
        }
        val arguments = AppHostBuilder()
                .setFactory(f)
                .recognizeClass(MyController::class.java)
                .parse(arrayOf("My", "action", "--param2", "value2", "--param3", "3", "--param1", "value1", "--param4", "3.4"))
        assertEquals(0, arguments.unRecognizedArguments.size.toLong())
        arguments.invoke().toArray()
        assertEquals(1, count.count.toLong())

    }

    @Test
    fun It_can_parse_class_and_default_method_and_execute() {
        val count = Counter()

        val arguments = AppHostBuilder()
                .setFactory (Function<Class<*>,Any>{ c ->
                    assertEquals(WithIndexController::class.java, c)
                    object : WithIndexController() {
                        override fun index(p: WithIndexController.Param): String? {
                            count.next()
                            return null
                        }
                    }
                })
                .recognizeClass(WithIndexController::class.java)
                .parse(arrayOf("WithIndex", /*
                     * "Index",
                     */ "--param2", "value2", "--param3", "3", "--param1", "value1", "--param4", "3.4"))

        assertEquals(0, arguments.unRecognizedArguments.size.toLong())
        arguments.invoke().toArray()
        assertEquals(1, count.count.toLong())
    }

    @Test
    fun It_can_invoke_recognized() {
        val count = Counter()

        AppHostBuilder()
                .parameter("beta", false, "", (Consumer { count.next() }))
                .parameter("alpha", false, "", (Consumer { fail() }))
                .parse(arrayOf("-a", "value", "--beta"))
                .invoke().toArray()
        assertEquals(1, count.count.toLong())
    }

    @Test
    fun It_understands_method_returning_enumerable() {
        val createCount = Counter()

        val arguments = AppHostBuilder().setFactory (Function<Class<*>,Any>{ c ->
            assertEquals(EnumerableController::class.java, c)
            createCount.next()
            object : EnumerableController() {

                override fun Return(): Stream<String>? {
                    return Arrays.asList(*arrayOf("1", "2")).stream()
                }
            }
        }).recognizeClass(EnumerableController::class.java).parse(arrayOf("enumerable", "return"))

        assertEquals(0, arguments.unRecognizedArguments.size.toLong())
        arguments.invoke().toArray()
        assertEquals(1, createCount.count.toLong())
    }

}
