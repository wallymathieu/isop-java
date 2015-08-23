package isop.api;

import isop.Build;

/**
 * Created by mathieu.
 */
public class ControllerExpression {
    private String controllerName;
    private Build build;
    public ControllerExpression(String controllerName, Build build){
        this.controllerName = controllerName;
        this.build = build;
    }
    public ActionControllerExpression action(String actionName)
    {
        return new ActionControllerExpression(controllerName, actionName, build);
    }
}
