package isop.api;

import isop.Build;
import isop.command_line.ControllerRecognizer;
import isop.command_line.parse.ArgumentParser;
import isop.command_line.parse.ParsedArguments;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by mathieu.
 */
public class ActionControllerExpression {

    private final String controllerName;
    private final String actionName;
    private final Build build;

    public ActionControllerExpression(String controllerName, String actionName, Build build) {
        this.controllerName = controllerName;
        this.actionName = actionName;
        this.build = build;
    }
    public ParsedArguments parameters(Map<String, String> arg){
        ArgumentParser argumentParser = new ArgumentParser(build.getGlobalParameters(),
                build._allowInferParameter);
        ParsedArguments parsedArguments = argumentParser.parse(arg);
        ControllerRecognizer controllerRecognizer = build.getControllerRecognizers()
                .values().stream()
                .map(Supplier::get)
                .filter(this::isRecognized)
                .findFirst()
                .orElse(null);
        if (null != controllerRecognizer){
            return controllerRecognizer.parseArgumentsAndMerge(actionName, arg,
                    parsedArguments);
        }

        parsedArguments.assertFailOnUnMatched();
        return parsedArguments;
    }

    private boolean isRecognized(ControllerRecognizer recognizer) {
        return recognizer.recognize(new String[]{controllerName, actionName});
    }

    /// <summary>
    ///
    /// </summary>
    public String help(){
        throw new NotImplementedException();
    }
}
