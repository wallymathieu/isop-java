package isop.test

import isop.AppHostBuilder
import isop.command_line.parse.ParsedArguments
import isop.test.fake_controllers.MyController
import isop.test.helpers.Counter
import isop.test.helpers.MapBuilder
import org.junit.Assert
import org.junit.Test

import java.util.function.Function

/**
 * Created by mathieu.
 */
class AlternativeApiTest {
    @Test
    fun It_can_parse_and_invoke() {
        val count = Counter()
        val factory = Function { t: Class<*> ->
            Assert.assertEquals(MyController::class.java, t)
            MyController()
                    .setOnAction { p -> "" + count.next() } as Any
        }
        val arguments = AppHostBuilder()
                .recognizeClass(MyController::class.java)
                .setFactory(factory)
                .controller("My")
                .action("Action")
                .parameters(MapBuilder<String, String>()
                        .put("param1", "value1")
                        .put("param2", "value2")
                        .put("param3", "3")
                        .put("param4", "3.4")
                        .map())

        Assert.assertEquals(0, arguments.unRecognizedArguments.size.toLong())

        arguments.invoke()
        Assert.assertEquals(1, count.count.toLong())
    }

    //@Test TODO: Implement
    fun It_can_get_help() {
        val count = Counter()
        val factory =Function { t: Class<*> ->
            Assert.assertEquals(MyController::class.java, t)
            MyController()
                    .setOnAction { p -> "" + count.next() } as Any
        }
        val help = AppHostBuilder()
                .recognizeClass(MyController::class.java)
                .setFactory(factory)
                .controller("My")
                .action("Action")
                .help()
        Assert.assertEquals("ActionHelp", help)
    }
}
