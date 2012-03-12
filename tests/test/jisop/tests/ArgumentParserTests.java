/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop.tests;

import java.util.Collection;
import jisop.Build;
import jisop.ParsedArguments;
import jisop.RecognizedArgument;
import jisop.UnrecognizedArgument;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author mathieu
 */
public class ArgumentParserTests {

    public ArgumentParserTests() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    public void Recognizes_shortform() {
        ParsedArguments parser = new Build().Parameter("&argument").Parse(new String[]{"-a"});
        Collection<RecognizedArgument> arguments = parser.RecognizedArguments;
        /*
         * Assert.That(arguments.size(), Is.EqualTo(1)); var arg1 =
         * arguments.First(); Assert.That(arg1.WithOptions.Argument.ToString(),
         * Is.EqualTo("&argument"));
         *
         */
        Assert.fail();
    }

    @Test
    public void Given_several_arguments_Then_the_correct_one_is_recognized() {
        Collection<RecognizedArgument> arguments = new Build().Parameter("&beta").Parse(new String[]{"-a", "-b"}).RecognizedArguments;
        /*
         * Assert.That(arguments.Count(), Is.EqualTo(1)); var arg1 =
         * arguments.First(); Assert.That(arg1.Argument, Is.EqualTo("b"));
         *
         */
        fail();
    }

    @Test
    public void Recognizes_longform() {
        Collection<RecognizedArgument> arguments = new Build().Parameter("beta").Parse(new String[]{"-a", "--beta"}).RecognizedArguments;
        /*
         * Assert.That(arguments.Count(), Is.EqualTo(1)); var arg1 =
         * arguments.First(); Assert.That(arg1.Argument, Is.EqualTo("beta"));
         *
         */
        fail();
    }

    @Test
    public void It_can_parse_parameter_value() {
        Collection<RecognizedArgument> arguments = new Build().Parameter("beta").Parse(new String[]{"-a", "--beta", "value"}).RecognizedArguments;
        /*
         * Assert.That(arguments.Count(), Is.EqualTo(1)); var arg1 =
         * arguments.First(); Assert.That(arg1.Argument, Is.EqualTo("beta"));
         * Assert.That(arg1.Value, Is.EqualTo("value"));
         *
         */
        fail();
    }

    @Test
    public void It_can_parse_ordinalparameters() {
        fail();
        //ArgumentParameter o;
        //Assert.That(OrdinalParameter.TryParse("#1first", CultureInfo.InvariantCulture, out o));
    }

    @Test
    public void It_can_parse_ordinal_parameter_value() {
        Collection<RecognizedArgument> arguments = new Build().Parameter("#0first").Parse(new String[]{"first"}).RecognizedArguments;
        fail();
        /*
         * Assert.That(arguments.Count(), Is.EqualTo(1)); var arg1 =
         * arguments.First(); Assert.That(arg1.Argument, Is.EqualTo("first"));
         */
    }

    @Test
    public void It_can_parse_parameter_with_equals() {
        Collection<RecognizedArgument> arguments = new Build().Parameter("beta=").Parse(new String[]{"-a", "--beta=test", "value"}).RecognizedArguments;
        /*
         * Assert.That(arguments.Count(), Is.EqualTo(1)); var arg1 =
         * arguments.First(); Assert.That(arg1.Value, Is.EqualTo("test"));
         * Assert.That(arg1.Argument, Is.EqualTo("beta"));
         */
        fail();
    }

    @Test
    public void It_can_parse_parameter_alias() {
        Collection<RecognizedArgument> arguments = new Build().Parameter("beta|b=").Parse(new String[]{"-a", "-b=test", "value"}).RecognizedArguments;
        /*
         * Assert.That(arguments.Count(), Is.EqualTo(1)); var arg1 =
         * arguments.First(); Assert.That(arg1.WithOptions.Argument.ToString(),
         * Is.EqualTo("beta|b=")); Assert.That(arg1.Value, Is.EqualTo("test"));
         * Assert.That(arg1.Argument, Is.EqualTo("b"));
         */
        fail();
    }

    @Test
    public void It_can_report_unrecognized_parameters() {
        Collection<UnrecognizedArgument> unRecognizedArguments = new Build().Parameter("beta").Parse(new String[]{"-a", "value", "--beta"}).UnRecognizedArguments;
        /*
         * Assert.That(unRecognizedArguments, Is.EquivalentTo(new[] { new
         * UnrecognizedArgument {Index = 0,Value = "-a"}, new
         * UnrecognizedArgument {Index = 1,Value = "value" } }));
         */
        fail();
    }

    @Test
    public void It_wont_report_matched_parameters() {
        Collection<UnrecognizedArgument> arguments = new Build().Parameter("beta").Parse(new String[]{"--beta", "value"}).UnRecognizedArguments;
        /*
         * Assert.That(arguments.Count(), Is.EqualTo(0));
         *
         */
        fail();
    }

    @Test
    public void It_will_fail_if_argument_not_supplied_and_it_is_required() {
        /*
         * Assert.Throws<MissingArgumentException>(() => new Build()
         * .Parameter("beta", required: true) .Parse(new[] { "-a", "value" }));
         *
         */
        fail();
    }

    @Test
    public void It_can_recognize_arguments() {
        Collection<RecognizedArgument> arguments = new Build().Parameter("alpha").Parse(new String[]{"alpha"}).RecognizedArguments;
        /*
         * Assert.That(arguments.Count(), Is.EqualTo(1)); var arg1 =
         * arguments.First(); Assert.That(arg1.WithOptions.Argument.ToString(),
         * Is.EqualTo("alpha")); Assert.That(arg1.Value, Is.Null);
         * Assert.That(arg1.Argument, Is.EqualTo("alpha"));
         *
         */
        fail();
    }

    @Test
    public void It_can_parse_class_and_method_and_execute() {
        /*
         * var count = 0; Func<Type, object> factory = (Type t) => {
         * Assert.That(t, Is.EqualTo(typeof(MyController))); return (object) new
         * MyController() { OnAction = (p1, p2, p3, p4) => (count++).ToString()
         * }; }; var arguments = new Build()
         * .SetCulture(CultureInfo.InvariantCulture)
         * .Recognize(typeof(MyController)) .SetFactory(factory) .Parse(new[] {
         * "My", "Action", "--param2", "value2", "--param3", "3", "--param1",
         * "value1", "--param4", "3.4" });
         *
         * Assert.That(arguments.UnRecognizedArguments.Count(), Is.EqualTo(0));
         * arguments.Invoke(new StringWriter()); Assert.That(count,
         * Is.EqualTo(1));
         */
        fail();
    }

    @Test
    public void It_can_parse_class_and_method_and_fail() {
        Build builder = new Build().RecognizeClass(MyController.class);
        fail();
        //Assert.Throws<MissingArgumentException>(() => builder.Parse(new[] { "My", "Action", "--param2", "value2", "--paramX", "3", "--param1", "value1", "--param4", "3.4" }));
    }

    class SingleIntAction {

        public void Action(int param) {
        }
    }

    @Test
    public void It_can_parse_class_and_method_and_fail_because_of_type_conversion() {
        Build builder = new Build().RecognizeClass(SingleIntAction.class);
        /*
         * Assert.Throws<TypeConversionFailedException>(() =>
         * builder.Parse(new[] { "SingleIntAction", "Action", "--param", "value"
         * }) );
         */
        fail();
    }

    @Test
    public void It_can_parse_class_and_method_and_fail_because_no_arguments_given() {
        Build builder = new Build().Recognize(MyController.class);

        //Assert.Throws<MissingArgumentException>(() => builder.Parse(new[] { "My", "Action" }));
        fail();
    }

    @Test
    public void It_can_parse_class_and_method_and_also_arguments_and_execute() {
        int count = 0;
        int countArg = 0;

        /*
         * Func<Type, object> factory = (Type t) => { Assert.That(t,
         * Is.EqualTo(typeof(MyController))); return (object) new MyController()
         * { OnAction = (p1, p2, p3, p4) => (count++).ToString() }; };
         *
         */
        ParsedArguments arguments = new Build().Recognize(MyController.class) //.Parameter("beta", arg => countArg++)
                //.SetFactory(factory)
                .Parse(new String[]{"My", "Action", "--param2", "value2", "--param3", "3", "--param1", "value1", "--param4", "3.4", "--beta"});

        /*
         * Assert.That(arguments.UnRecognizedArguments.Count(), Is.EqualTo(0));
         * arguments.Invoke(new StringWriter()); Assert.That(count,
         * Is.EqualTo(1)); Assert.That(countArg, Is.EqualTo(1));
         */
        fail();
    }

    @Test
    public void It_can_parse_class_and_default_method_and_execute() {
        int count = 0;
        /*
         * Func<Type, object> factory = (Type t) => { Assert.That(t,
         * Is.EqualTo(typeof(WithIndexController))); return (object) new
         * WithIndexController() { OnIndex = (p1, p2, p3, p4) =>
         * (count++).ToString() }; };
         *
         */

        ParsedArguments arguments = new Build().Recognize(WithIndexController.class) //.SetFactory(factory)
                .Parse(new String[]{"WithIndex", /*
                     * "Index",
                     */ "--param2", "value2", "--param3", "3", "--param1", "value1", "--param4", "3.4"});
        /*
         * Assert.That(arguments.UnRecognizedArguments.Count(), Is.EqualTo(0));
         * arguments.Invoke(new StringWriter()); Assert.That(count,
         * Is.EqualTo(1));
         */
        fail();
    }

    private class WithIndexController {

        public WithIndexController() {
            //  OnIndex = (p1, p2, p3, p4) => string.Empty;
        }
        //public Func<string, string, int, decimal, string> OnIndex { get; set; }

        public String Index(String param1, String param2, int param3, double param4) {
            //    return OnIndex(param1, param2, param3, param4); 
            return null;
        }
    }

    @Test
    public void It_can_invoke_recognized() {
        int count = 0;

        new Build() //                .Parameter("beta", arg => count++)
                //              .Parameter("alpha", arg => Assert.Fail())
                .Parse(new String[]{"-a", "value", "--beta"}).Invoke(System.out);
        fail();
        //Assert.That(count, Is.EqualTo(1));
    }

    @Test
    public void It_understands_method_returning_enumerable() {
        int count = 0;
        int createCount = 0;
        /*
         * Func<Type, object> factory = (Type t) => { Assert.That(t,
         * Is.EqualTo(typeof(EnumerableController))); createCount++; return
         * (object) new EnumerableController() { Length = 2, OnEnumerate = () =>
         * (count++) }; };
         */

        ParsedArguments arguments = new Build().Recognize(EnumerableController.class) //.SetFactory(factory)
                .Parse(new String[]{"Enumerable", "Return"});

        /*
         * Assert.That(arguments.UnRecognizedArguments.Count(), Is.EqualTo(0));
         * arguments.Invoke(new StringWriter()); Assert.That(count,
         * Is.EqualTo(2)); Assert.That(createCount, Is.EqualTo(1));
         */
        fail();
    }

    class EnumerableController {
        //public Func<Object> OnEnumerate;

        public int Length;

        public Collection Return() {
            /*
             * for (int i = 0; i < Length; i++) { yield return OnEnumerate(); }
             */
            return null;
        }
    }
}
