package jisop.tests;



import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.rmi.server.LogStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import jisop.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author mathieu
 */
public class ArgumentParserTests {

    public ArgumentParserTests() {
    }

    @Test
    public void Recognizes_shortform() {
        Collection<RecognizedArgument> arguments = new Build().parameter("&argument").parse(new String[]{"-a"}).recognizedArguments;
        assertEquals(1, arguments.size());
        RecognizedArgument arg1 = arguments.iterator().next();
        assertEquals("&argument", arg1.withOptions.argument.toString());
    }

    @Test
    public void Given_several_arguments_Then_the_correct_one_is_recognized() {
        Collection<RecognizedArgument> arguments = new Build().parameter("&beta").parse(new String[]{"-a", "-b"}).recognizedArguments;
        assertEquals(1, arguments.size());
        RecognizedArgument first = arguments.iterator().next();
        assertEquals("b", first.argument);
    }

    @Test
    public void Recognizes_longform() {
        Collection<RecognizedArgument> arguments = new Build().parameter("beta").parse(new String[]{"-a", "--beta"}).recognizedArguments;
        assertEquals(1, arguments.size());
        RecognizedArgument first = arguments.iterator().next();
        assertEquals("beta", first.argument);
    }

    @Test
    public void It_can_parse_parameter_value() {
        Collection<RecognizedArgument> arguments = new Build().parameter("beta").parse(new String[]{"-a", "--beta", "value"}).recognizedArguments;
        assertEquals(1, arguments.size());
        RecognizedArgument first = arguments.iterator().next();
        assertEquals("beta", first.argument);
        assertEquals("value", first.value);
    }

    @Test
    public void It_can_parse_ordinalparameters() {
        ArgumentParameter o = OrdinalParameter.tryParse("#1first");
        assertNotNull(o);
    }

    @Test
    public void It_can_parse_ordinal_parameter_value() {
        Collection<RecognizedArgument> arguments = new Build().parameter("#0first").parse(new String[]{"first"}).recognizedArguments;
        assertEquals(1, arguments.size());
        RecognizedArgument first = arguments.iterator().next();
        assertEquals("first", first.argument);
    }

    @Test
    public void It_can_parse_parameter_with_equals() {
        Collection<RecognizedArgument> arguments = new Build().parameter("beta=").parse(new String[]{"-a", "--beta=test", "value"}).recognizedArguments;
        assertEquals(1, arguments.size());
        RecognizedArgument first = arguments.iterator().next();
        assertEquals("beta", first.argument);
        assertEquals("test", first.value);
    }

    @Test
    public void It_can_parse_parameter_alias() {
        Collection<RecognizedArgument> arguments = new Build().parameter("beta|b=").parse(new String[]{"-a", "-b=test", "value"}).recognizedArguments;
        System.out.println(arguments);
        assertEquals(1, arguments.size());
        RecognizedArgument first = arguments.iterator().next();
        assertEquals("beta|b=", first.withOptions.argument.toString());
        assertEquals("b", first.argument);
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
    public void It_wont_report_matched_parameters() {
        Collection<UnrecognizedArgument> arguments = new Build().parameter("beta").parse(new String[]{"--beta", "value"}).unRecognizedArguments;
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
        assertEquals("alpha", arg1.withOptions.argument.toString());
        assertEquals(null, arg1.value);
        assertEquals("alpha", arg1.argument);
    }

    @Test
    public void It_can_parse_class_and_method_and_execute() {
        __count = 0;

        ObjectFactory factory = new ObjectFactory() {

            @Override
            public Object build(Class c) {
                assertEquals(MyController.class, c);
                return new MyController() {

                    @Override
                    public String Action(MyController.Param p) {
                        __count++;
                        return null;
                    }
                };
            }
        };
        ParsedArguments arguments = new Build()
                .RecognizeClass(MyController.class)
                .setFactory(factory)
                .parse(new String[]{"My", "Action", "--param2", "value2", "--param3", "3", "--param1", "value1", "--param4", "3.4"});
        assertEquals(0, arguments.unRecognizedArguments.size());
        arguments.invoke(null);
        assertEquals(1, __count);
    }

    @Test
    public void It_can_parse_class_and_method_and_fail() {
        Build builder = new Build().RecognizeClass(MyController.class);
        try {
            builder.parse(new String[]{"My", "Action", "--param2", "value2", "--paramX", "3", "--param1", "value1", "--param4", "3.4"});
            fail();
        } catch (MissingArgumentException e) {
        }
    }

    class SingleIntAction {
        public class Param{
            public int param;
        }
        public void Action(Param param) {
        }
    }

    @Test
    public void It_can_parse_class_and_method_and_fail_because_of_type_conversion() {
        Build builder = new Build().setFactory(new ObjectFactory() {

            @Override
            public Object build(Class c) {
                return new SingleIntAction();
            }
        }).RecognizeClass(SingleIntAction.class);
        try {
            builder.parse(new String[]{"SingleIntAction", "Action", "--param", "value"});
            fail();
        } catch (TypeConversionFailedException e) {
        }
    }

    @Test
    public void It_can_parse_class_and_method_and_fail_because_no_arguments_given() {
        Build builder = new Build().RecognizeClass(MyController.class);
        try {
            builder.parse(new String[]{"My", "Action"});
            fail();
        } catch (MissingArgumentException e) {
        }
    }
    private int __count;

    @Test
    public void It_can_parse_class_and_method_and_also_arguments_and_execute() {
        __count = 0;
        ObjectFactory f = new ObjectFactory() {

            public Object build(Class c) {
                assertEquals(MyController.class, c);
                return new MyController() {

                    @Override
                    public String Action(MyController.Param p) {
                        __count++;
                        return null;
                    }
                };
            }
        };
        OutputStream out = new OutputStream() {

            public void write(int b) {
            }
        };
        ParsedArguments arguments = new Build().setFactory(f).RecognizeClass(MyController.class) //.Parameter("beta", arg => countArg++)
                .parse(new String[]{"My", "Action", "--param2", "value2", "--param3", "3", "--param1", "value1", "--param4", "3.4", "--beta"});
        assertEquals(0, arguments.unRecognizedArguments.size());
        arguments.invoke(out);
        assertEquals(1, __count);

    }

    @Test
    public void It_can_parse_class_and_default_method_and_execute() {
        __count = 0;

        ParsedArguments arguments = new Build().setFactory(new ObjectFactory() {

            @Override
            public Object build(Class c) {
                assertEquals(WithIndexController.class, c);
                return new WithIndexController() {

                    @Override
                    public String Index(WithIndexController.Param p) {
                        __count++;
                        return null;
                    }
                };
            }
        }).RecognizeClass(WithIndexController.class).parse(new String[]{"WithIndex", /*
                     * "Index",
                     */ "--param2", "value2", "--param3", "3", "--param1", "value1", "--param4", "3.4"});

        assertEquals(0, arguments.unRecognizedArguments.size());
        arguments.invoke(new OutputStream() {

            @Override
            public void write(int i) throws IOException {
            }
        });
        assertEquals(1,__count);
    }

    private class WithIndexController {
        public class Param
        {
            public String param1;
            public String param2;
            public int param3;
            public double param4;
        }

        public WithIndexController() {
        }
        public String Index(Param p) {
            //    return OnIndex(param1, param2, param3, param4); 
            return null;
        }
    }

    @Test
    public void It_can_invoke_recognized() {
        __count = 0;

        new Build().parameter("beta", false, "", new ArgumentAction() {

            @Override
            public void invoke(String value) {
                __count++;
            }
        }).parameter("alpha", false, "", new ArgumentAction() {

            @Override
            public void invoke(String value) {
                fail();
            }
        }).parse(new String[]{"-a", "value", "--beta"}).invoke(System.out);
        assertEquals(1, __count);
    }
    int __createCount;

    @Test
    public void It_understands_method_returning_enumerable() {
        __createCount = 0;

        ParsedArguments arguments = new Build().RecognizeClass(EnumerableController.class).setFactory(new ObjectFactory() {

            @Override
            public Object build(Class c) {
                assertEquals(EnumerableController.class, c);
                __createCount++;
                return new EnumerableController() {

                    @Override
                    public String[] Return() {
                        return new String[]{"1", "2"};
                    }
                };
            }
        }).parse(new String[]{"Enumerable", "Return"});

        assertEquals(0, arguments.unRecognizedArguments.size());
        arguments.invoke(null);
        assertEquals(1, __createCount);
    }

    class EnumerableController {
        //public Func<Object> OnEnumerate;

        public int Length;

        public String[] Return() {
            /*
             * for (int i = 0; i < Length; i++) { yield return OnEnumerate(); }
             */
            return null;
        }
    }
}
