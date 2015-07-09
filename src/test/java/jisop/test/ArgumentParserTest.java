package jisop.test;



import jisop.Build;
import jisop.MissingArgumentException;
import jisop.TypeConversionFailedException;
import jisop.command_line.ControllerRecognizer;
import jisop.command_line.parse.*;
import jisop.domain.Argument;
import jisop.test.fake_controllers.EnumerableController;
import jisop.test.fake_controllers.MyController;
import jisop.test.fake_controllers.MyOptionalController;
import jisop.test.fake_controllers.WithIndexController;
import jisop.test.helpers.Counter;
import jisop.test.testData.SingleIntAction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.Assert.*;
/**
 *
 * @author mathieu
 */
public class ArgumentParserTest extends Base {

    public ArgumentParserTest() {
    }

    @Test
    public void Recognizes_shortform() {
        Collection<RecognizedArgument> arguments = new Build().parameter("&argument").parse(new String[]{"-a"}).recognizedArguments;
        assertEquals(1, arguments.size());
        RecognizedArgument arg1 = arguments.iterator().next();
        assertEquals("argument", arg1.argument.name);
    }

    @Test
    public void Given_several_arguments_Then_the_correct_one_is_recognized() {
        Collection<RecognizedArgument> arguments = new Build().parameter("&beta").parse(new String[]{"-a", "-b"}).recognizedArguments;
        assertEquals(1, arguments.size());
        RecognizedArgument first = arguments.iterator().next();
        assertEquals("b", first.rawArgument);
    }

    @Test
    public void Recognizes_longform() {
        Collection<RecognizedArgument> arguments = new Build().parameter("beta").parse(new String[]{"-a", "--beta"}).recognizedArguments;
        assertEquals(1, arguments.size());
        RecognizedArgument first = arguments.iterator().next();
        assertEquals("beta", first.rawArgument);
    }

    @Test
    public void It_can_parse_parameter_value() {
        Collection<RecognizedArgument> arguments = new Build()
                .parameter("beta")
                .parse(new String[]{"-a", "--beta", "value"})
                .recognizedArguments;
        assertEquals(1, arguments.size());
        RecognizedArgument first = arguments.iterator().next();
        assertEquals("beta", first.rawArgument);
        assertEquals("value", first.value);
    }

    @Test
    public void It_can_parse_ordinalparameters() {
        ArgumentParameter o = OrdinalParameter.tryParse("#1first");
        assertNotNull(o);
    }

    @Test
    public void It_can_parse_ordinal_parameter_value() {
        Collection<RecognizedArgument> arguments = new Build()
                .parameter("#0first")
                .parse(new String[]{"first"})
                .recognizedArguments;
        assertEquals(1, arguments.size());
        RecognizedArgument first = arguments.iterator().next();
        assertEquals("first", first.rawArgument);
    }

    @Test
    public void It_can_parse_parameter_with_equals() {
        Collection<RecognizedArgument> arguments = new Build().parameter("beta=").parse(new String[]{"-a", "--beta=test", "value"}).recognizedArguments;
        assertEquals(1, arguments.size());
        RecognizedArgument first = arguments.iterator().next();
        assertEquals("beta", first.rawArgument);
        assertEquals("test", first.value);
    }

    @Test
    public void It_can_parse_parameter_alias() {
        Collection<RecognizedArgument> arguments = new Build()
                .parameter("beta|b=")
                .parse(new String[]{"-a", "-b=test", "value"})
                .recognizedArguments;
        assertEquals(1, arguments.size());
        RecognizedArgument first = arguments.iterator().next();
        assertEquals("beta", first.argument.name);
        assertEquals("b", first.rawArgument);
        assertEquals("test", first.value);
    }

    @Test
    public void It_can_report_unrecognized_parameters() {
        Collection<UnrecognizedArgument> unRecognizedArguments = new Build().parameter("beta").parse(new String[]{"-a", "value", "--beta"}).unRecognizedArguments;
        UnrecognizedArgument[] expected = new UnrecognizedArgument[]{
            new UnrecognizedArgument(0, "-a"),
            new UnrecognizedArgument(1, "value")
        };
        assertArrayEquals(expected, unRecognizedArguments.toArray());
    }
    @Test
    public void It_can_infer_ordinal_usage_of_named_parameters()
    {
        Collection<RecognizedArgument> arguments = new Build()
                .parameter("beta|b=")
                .parameter("alpha|a=")
                .parse(new String[] { "test", "value" })
                .recognizedArguments;
        assertArrayEquals(new String[]{"test", "value" },
                arguments.stream().map(arg->arg.value).toArray());
    }

    @Test
    public void It_wont_report_matched_parameters() {
        Collection<UnrecognizedArgument> arguments = new Build()
                .parameter("beta")
                .parse(new String[]{"--beta", "value"})
                .unRecognizedArguments;
        assertEquals(0, arguments.size());
    }

    @Test
    public void It_will_fail_if_argument_not_supplied_and_it_is_required() {
        try {
            new Build().parameter("beta", true).parse(new String[]{"-a", "value"});
            fail();
        } catch (MissingArgumentException e) {
        }
    }

    @Test
    public void It_can_recognize_arguments() {
        Collection<RecognizedArgument> arguments = new Build().parameter("alpha").parse(new String[]{"alpha"}).recognizedArguments;

        assertEquals(1, arguments.size());
        RecognizedArgument arg1 = arguments.iterator().next();
        assertEquals("alpha", arg1.argument.name);
        assertEquals(null, arg1.value);
        assertEquals("alpha", arg1.rawArgument);
    }

    @Test
    public void It_can_parse_class_and_method_and_execute() {
        Counter count = new Counter();

        Function<Class,Object> factory = c -> {
            assertEquals(MyController.class, c);
            return new MyController() {

                @Override
                public String action(Param p) {
                    count.next();
                    return null;
                }
            };
        };
        ParsedArguments arguments = new Build()
                .setFactory(factory)
                .recognizeClass(MyController.class)
                .parse(new String[]{"my", "action", "--param2", "value2", "--param3", "3", "--param1", "value1", "--param4", "3.4"});
        assertEquals(0, arguments.unRecognizedArguments.size());
        arguments.invoke().toArray();
        assertEquals(1, count.getCount());
    }

    @Test
    public void It_can_parse_class_and_method_and_execute_with_ordinal_syntax()
    {
        Counter count = new Counter();
        Function<Class, Object> factory = (Class t) ->
        {
            assertEquals(MyController.class,t);
            return
                    (Object)
                            new MyController().setOnAction((p1) -> toString(count.next()));
        };
        ParsedArguments arguments = new Build()
            .recognizeClass(MyController.class)
            .setFactory(factory)
            .parse(new String[]{"My", "action", "value1", "value2", "3", "3.4"});

        assertEquals(0, arguments.unRecognizedArguments.size());
        arguments.invoke().toArray();
        assertEquals(1, count.getCount());
    }

    @Test
    public void It_can_parse_class_and_method_and_knows_whats_required()
    {
        Function<Class, Object> factory = (Class t) ->
        {
            assertEquals(MyController.class, t);
            return
                    (Object)
                            new MyController().setOnAction((p1) -> "");
        };
        Build build = new Build()
                .recognizeClass(MyController.class)
                .setFactory(factory);

        ControllerRecognizer first = build.getControllerRecognizers()
                .values().stream()
                .findFirst().get()
                .get();
        List<Argument> recognizers = first.getRecognizers("action");
        assertArrayEquals(
                dictionaryDescriptionToKv("[param1, true], [param2, true], [param3, true], [param4, true]", s -> Boolean.parseBoolean(s)).toArray(),
                recognizers.stream().map(r -> toSimpleEntry(r.name, r.required)).toArray());
    }

    @Test
    public void It_can_parse_class_and_method_and_knows_whats_not_required()
    {
        Function<Class, Object> factory = (Class t) ->
        {
            assertEquals(MyController.class, t);
            return
                    (Object)
                            new MyOptionalController().SetOnAction((p1) -> "" );
        };
        Build build = new Build()
                .recognizeClass(MyOptionalController.class)
                .setFactory(factory);

        ControllerRecognizer first = build.getControllerRecognizers()
                .values().stream()
                .findFirst().get()
                .get();
        List<Argument> recognizers = first.getRecognizers("action");
        assertArrayEquals(
                dictionaryDescriptionToKv("[param1, true], [param2, false], [param3, false], [param4, false]", s -> Boolean.parseBoolean(s)).toArray(),
                recognizers.stream().map(r-> toSimpleEntry(r.name,r.required)).toArray() );
    }

    //@Test TODO: Not implemented!
    public void It_can_parse_class_and_method_and_executes_default_with_the_default_values()
    {
        List<MyOptionalController.Param> parameters=new ArrayList<>();
        Function<Class, Object> factory = (Class t) ->
        {
            assertEquals(MyOptionalController.class, t);
            return
                    (Object)
                            new MyOptionalController().SetOnAction((p1) ->{ parameters.add(p1); return "";} );
        };
        ParsedArguments arguments = new Build()
                .recognizeClass(MyOptionalController.class)
                .setFactory(factory)
                .parse(new String[]{"MyOptional", "action", "--param1", "value1"});

        arguments.invoke().toArray();
        assertArrayEquals(new Object[]{"value1", null, null, 1 }, parameters.get(0).toArray());
    }

    //@Test TODO: Not implemented!
    public void It_can_parse_class_and_method_and_executes_default_with_the_default_values_when_using_ordinal_syntax()
    {
        List<MyOptionalController.Param> parameters=new ArrayList<>();
        Function<Class, Object> factory = (Class t) ->
        {
            assertEquals(MyOptionalController.class, t);
            return
                    (Object)
                            new MyOptionalController().SetOnAction((p1) ->{ parameters.add(p1); return "";} );
        };
        ParsedArguments arguments = new Build()
                .recognizeClass(MyOptionalController.class)
                .setFactory(factory)
                .parse(new String[]{"MyOptional", "action", "value1"});

        arguments.invoke().toArray();
        assertArrayEquals(new Object[]{"value1", null, null, 1}, parameters.get(0).toArray());
    }


    @Test
    public void It_can_parse_class_and_method_and_fail() {
        Build builder = new Build().recognizeClass(MyController.class);
        try {
            builder.parse(new String[]{"My", "action", "--param2", "value2", "--paramX", "3", "--param1", "value1", "--param4", "3.4"});
            fail();
        } catch (MissingArgumentException e) {
        }
    }

    @Test
    public void It_can_parse_class_and_method_and_fail_because_of_type_conversion() {
        Build builder = new Build()
                .setFactory(c -> new SingleIntAction())
                .recognizeClass(SingleIntAction.class);
        try {
            builder.parse(new String[]{"SingleIntAction", "action", "--param", "value"});
            fail();
        } catch (TypeConversionFailedException e) {
        }
    }

    @Test
    public void It_can_parse_class_and_method_and_fail_because_no_arguments_given() {
        Build builder = new Build().recognizeClass(MyController.class);
        try {
            builder.parse(new String[]{"My", "action"});
            fail();
        } catch (MissingArgumentException e) {
        }
    }

    @Test
    public void It_can_parse_class_and_method_and_also_arguments_and_execute() {
        Counter count = new Counter();
        Function<Class,Object> f = c -> {
            assertEquals(MyController.class, c);
            return new MyController() {

                @Override
                public String action(Param p) {
                    count.next();
                    return null;
                }
            };
        };
        ParsedArguments arguments = new Build()
                .setFactory(f)
                .recognizeClass(MyController.class)
                .parse(new String[]{"My", "action", "--param2", "value2", "--param3", "3", "--param1", "value1", "--param4", "3.4"});
        assertEquals(0, arguments.unRecognizedArguments.size());
        arguments.invoke().toArray();
        assertEquals(1, count.getCount());

    }

    @Test
    public void It_can_parse_class_and_default_method_and_execute() {
        Counter count = new Counter();

        ParsedArguments arguments = new Build()
                .setFactory(c -> {
                    assertEquals(WithIndexController.class, c);
                    return new WithIndexController() {

                        @Override
                        public String Index(Param p) {
                            count.next();
                            return null;
                        }
                    };
                })
                .recognizeClass(WithIndexController.class)
                .parse(new String[]{"WithIndex", /*
                     * "Index",
                     */ "--param2", "value2", "--param3", "3", "--param1", "value1", "--param4", "3.4"});

        assertEquals(0, arguments.unRecognizedArguments.size());
        arguments.invoke().toArray();
        assertEquals(1, count.getCount());
    }

    @Test
    public void It_can_invoke_recognized() {
        Counter count= new Counter();

        new Build()
                .parameter("beta", false, "", value -> count.next())
                .parameter("alpha", false, "", value -> fail())
                .parse(new String[]{"-a", "value", "--beta"})
                .invoke().toArray();
        assertEquals(1, count.getCount());
    }

    @Test
    public void It_understands_method_returning_enumerable() {
        Counter createCount = new Counter();

        ParsedArguments arguments = new Build().setFactory(c -> {
            assertEquals(EnumerableController.class, c);
            createCount.next();
            return new EnumerableController() {

                @Override
                public Stream<String> Return() {
                    return Arrays.asList(new String[]{"1", "2"}).stream();
                }
            };
        }).recognizeClass(EnumerableController.class).parse(new String[]{"enumerable", "return"});

        assertEquals(0, arguments.unRecognizedArguments.size());
        arguments.invoke().toArray();
        assertEquals(1, createCount.getCount());
    }

}
