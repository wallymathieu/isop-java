package isop.test.fake_controllers;

import isop.test.helpers.NotNull;

import java.util.function.Function;

/**
 * Created by mathieu.
 */
public class MyOptionalController {
    public class Param{
        @NotNull
        public String param1;
        public String param2;
        public int param3;
        public double param4;
        public Object[] toArray(){
            return new Object[]{param1,param2,param3,param4};
        }
    }
    public Function<Param,String> onAction;
    public MyOptionalController setOnAction(Function<Param, String> onAction){
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
