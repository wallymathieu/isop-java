package jisop.api;

import jisop.*;
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
        throw new NotImplementedException();
    }
    /// <summary>
    ///
    /// </summary>
    public String Help()
    {
        throw new NotImplementedException();
    }
}
