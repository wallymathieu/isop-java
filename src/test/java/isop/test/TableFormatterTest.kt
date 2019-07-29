package isop.test

import isop.AppHostBuilder
import isop.command_line.parse.ParsedArguments
import isop.infrastructure.Strings
import isop.test.fake_controllers.ObjectController
import isop.test.helpers.Counter
import isop.test.testData.WithTwoFields
import org.junit.Assert
import org.junit.Test

import java.util.Arrays
import java.util.function.Function
import java.util.function.Supplier

/**
 * Created by mathieu.
 */
class TableFormatterTest {
    @Test
    fun It_can_format_object_as_table() {
        val count = Counter()
        val factory = Function<Class<*>,Any>{ t: Class<*> ->
            Assert.assertEquals(ObjectController::class.java, t)
            val o = ObjectController()
            o.onAction = Supplier{ WithTwoFields(count.next()) }
            o
        }
        val arguments = AppHostBuilder()
                .recognizeClass(ObjectController::class.java)
                .setFactory(factory)
                .formatObjectsAsTable()
                .parse(arrayOf("Object", "Action"))

        Assert.assertEquals(0, arguments.unRecognizedArguments.size.toLong())
        val result = Strings.join("\n", arguments.invoke())
        Assert.assertEquals("first\tsecond\n1\tV1",
                result)
    }

    @Test
    fun It_can_format_array_of_objects_as_table() {
        val count = Counter()
        val factory = Function<Class<*>,Any> { t: Class<*> ->
            Assert.assertEquals(ObjectController::class.java, t)
            val o = ObjectController()
            o.onAction =Supplier { arrayOf(WithTwoFields(count.next()), WithTwoFields(count.next())) }
            o
        }
        val arguments = AppHostBuilder()
                .recognizeClass(ObjectController::class.java)
                .setFactory(factory)
                .formatObjectsAsTable()
                .parse(arrayOf("Object", "Action"))

        Assert.assertEquals(0, arguments.unRecognizedArguments.size.toLong())
        val result = Strings.join("\n", arguments.invoke())
        Assert.assertEquals("first\tsecond\n1\tV1\n2\tV2",
                result)
    }

    @Test
    fun It_can_format_collection_of_objects_as_table() {
        val count = Counter()
        val factory = Function<Class<*>,Any>{ t: Class<*> ->
            Assert.assertEquals(ObjectController::class.java, t)
            val o = ObjectController()
            o.onAction = Supplier {
                Arrays.asList(
                        WithTwoFields(count.next()),
                        WithTwoFields(count.next()))
            }
            o
        }
        val arguments = AppHostBuilder()
                .recognizeClass(ObjectController::class.java)
                .setFactory(factory)
                .formatObjectsAsTable()
                .parse(arrayOf("Object", "Action"))

        Assert.assertEquals(0, arguments.unRecognizedArguments.size.toLong())
        val result = Strings.join("\n", arguments.invoke())
        Assert.assertEquals("first\tsecond\n1\tV1\n2\tV2",
                result)
    }
}
