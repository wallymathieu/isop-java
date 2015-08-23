package isop.test.fake_controllers;

import java.util.function.Supplier;

/**
 * Created by mathieu.
 */
public class ObjectController {
    public ObjectController(){
        onAction = ()->"";
    }
    public Supplier<Object> onAction;
    public Object action() { return onAction.get(); }
}
