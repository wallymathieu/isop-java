package jisop.api;

import jisop.*;
import jisop.command_line.ControllerRecognizer;
import jisop.command_line.parse.ArgumentParser;
import jisop.command_line.parse.ParsedArguments;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Dictionary;
import java.util.Map;

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
    public ParsedArguments parameters(Map<String, String> arg)
    {
        ArgumentParser argumentParser = new ArgumentParser(build.getGlobalParameters(), build._allowInferParameter);
        ParsedArguments parsedArguments = argumentParser.parse(arg);
        ControllerRecognizer controllerRecognizer = build.getControllerRecognizers()
                .values().stream()
                .map(recognizer -> recognizer.get())
                .filter(recognizer -> recognizer.recognize(new String[]{controllerName, actionName}))
                .findFirst()
                .orElse(null);
        if (null != controllerRecognizer)
        {
            return controllerRecognizer.parseArgumentsAndMerge(actionName, arg,
                    parsedArguments);
        }

        parsedArguments.assertFailOnUnMatched();
        return parsedArguments;
    }
    /// <summary>
    ///
    /// </summary>
    public String help()
    {
        throw new NotImplementedException();
    }
}
