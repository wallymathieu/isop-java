package isop.test;

import isop.Build;
import isop.command_line.parse.ParsedArguments;
import isop.infrastructure.Strings;
import isop.test.fake_controllers.ObjectController;
import isop.test.helpers.Counter;
import isop.test.testData.WithTwoFields;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Created by mathieu.
 */
public class TableFormatterTest {
    @Test
    public void It_can_format_object_as_table() {
        Counter count = new Counter();
        Function<Class, Object> factory = (Class t) ->{
            Assert.assertEquals(ObjectController.class, t);
            ObjectController o= new ObjectController();
            o.onAction = () -> new WithTwoFields(count.next());
            return (Object)o;
        };
        ParsedArguments arguments = new Build()
            .recognizeClass(ObjectController.class)
            .setFactory(factory)
            .formatObjectsAsTable()
            .parse(new String[] { "Object", "Action" });

        Assert.assertEquals(0, arguments.unRecognizedArguments.size());
        String result = Strings.join("\n", arguments.invoke());
        Assert.assertEquals("first\tsecond\n1\tV1",
            result);
    }

    @Test
    public void It_can_format_array_of_objects_as_table(){
        Counter count = new Counter();
        Function<Class, Object> factory = (Class t) -> {
            Assert.assertEquals(ObjectController.class, t);
            ObjectController o= new ObjectController();
            o.onAction = () -> new WithTwoFields[]{
                    new WithTwoFields(count.next()),
                    new WithTwoFields(count.next())
            };
            return (Object)o;
        };
        ParsedArguments arguments = new Build()
            .recognizeClass(ObjectController.class)
            .setFactory(factory)
            .formatObjectsAsTable()
            .parse(new String[]{"Object", "Action"});

        Assert.assertEquals(0, arguments.unRecognizedArguments.size());
        String result = Strings.join("\n", arguments.invoke());
        Assert.assertEquals("first\tsecond\n1\tV1\n2\tV2",
                result);
    }
    @Test
    public void It_can_format_collection_of_objects_as_table(){
        Counter count = new Counter();
        Function<Class, Object> factory = (Class t) -> {
            Assert.assertEquals(ObjectController.class, t);
            ObjectController o= new ObjectController();
            o.onAction = () -> Arrays.asList(
                    new WithTwoFields(count.next()),
                    new WithTwoFields(count.next()));
            return (Object)o;
        };
        ParsedArguments arguments = new Build()
                .recognizeClass(ObjectController.class)
                .setFactory(factory)
                .formatObjectsAsTable()
                .parse(new String[]{"Object", "Action"});

        Assert.assertEquals(0, arguments.unRecognizedArguments.size());
        String result = Strings.join("\n", arguments.invoke());
        Assert.assertEquals("first\tsecond\n1\tV1\n2\tV2",
                result);
    }
}
