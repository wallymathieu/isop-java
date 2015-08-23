package isop.test;

import isop.Build;
import isop.command_line.parse.ParsedArguments;
import isop.test.fake_controllers.MyController;
import isop.test.helpers.Counter;
import isop.test.helpers.MapBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;

/**
 * Created by mathieu.
 */
public class AlternativeApiTest {
   @Test
   public void It_can_parse_and_invoke()
   {
       Counter count = new Counter();
       Function<Class, Object> factory = (Class t) ->
       {
           Assert.assertEquals(MyController.class, t);
           return (Object)new MyController()
               .setOnAction(p->""+ count.next());
       };
       ParsedArguments arguments = new Build()
            .recognizeClass(MyController.class)
            .setFactory(factory)
            .controller("My")
            .action("Action")
            .parameters(new MapBuilder<String, String>()
                    .put("param1", "value1")
                    .put("param2", "value2")
                    .put("param3", "3")
                    .put("param4", "3.4")
                    .map());

       Assert.assertEquals(0, arguments.unRecognizedArguments.size());

       arguments.invoke();
       Assert.assertEquals(1, count.getCount());
   }

    //@Test TODO: Implement
    public void It_can_get_help()
    {
        Counter count = new Counter();
        Function<Class, Object> factory = (Class t) ->
        {
            Assert.assertEquals(MyController.class, t);
            return (Object)new MyController()
                    .setOnAction(p->""+ count.next());
        };
        String help = new Build()
                .recognizeClass(MyController.class)
                .setFactory(factory)
                .controller("My")
                .action("Action")
                .help();
        Assert.assertEquals("ActionHelp", help);
    }
}
