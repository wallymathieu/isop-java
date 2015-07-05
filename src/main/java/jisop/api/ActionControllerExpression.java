package jisop.api;

import jisop.*;
import jisop.command_line.ControllerRecognizer;
import jisop.command_line.parse.ArgumentParser;
import jisop.command_line.parse.ParsedArguments;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Dictionary;
/**
 * Created by mathieu.
 */
public class ActionControllerExpression {

    private String controllerName;
    private String actionName;
    private Build build;

    public ActionControllerExpression(String controllerName, String actionName, Build build)
    {
        this.controllerName = controllerName;
        this.actionName = actionName;
        this.build = build;
    }
    public ParsedArguments Parameters(Dictionary<String, String> arg)
    {
        ArgumentParser argumentParser = new ArgumentParser(build.getGlobalParameters());
        ParsedArguments parsedArguments = argumentParser.Parse(arg);
        if (build.getControllerRecognizers().stream()
                .anyMatch(a -> true))
        {
            ControllerRecognizer controllerRecognizer = build.getControllerRecognizers().stream()
                    .filter(recognizer -> recognizer.recognize(new String[]{controllerName, actionName}))
                    .findFirst()
                    .get();
            if (null != controllerRecognizer)
            {
                return controllerRecognizer.parseArgumentsAndMerge(actionName, arg,
                        parsedArguments);
            }
        }
        parsedArguments.assertFailOnUnMatched();
        return parsedArguments;
    }
    /// <summary>
    ///
    /// </summary>
    public String Help()
    {
        throw new NotImplementedException();
    }
}
