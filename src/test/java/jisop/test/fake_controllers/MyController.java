package jisop.test.fake_controllers;

import java.util.function.*;

/**
 *
 * @author mathieu
 */
public class MyController {

    public MyController() {
    }
    public class Param{
        public String param1;
        public String param2; 
        public int param3;
        public double param4;
    }
    public Function<Param,String> onAction;
    public MyController setOnAction(Function<Param, String> onAction){
        this.onAction = onAction;
        return this;
    }
    public String action(Param p) {
        if (onAction!=null){
            return onAction.apply(p);
        }
        return null;
    }
}
