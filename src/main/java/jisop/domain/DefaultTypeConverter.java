package jisop.domain;

import java.util.function.BiFunction;

/**
 * Created by mathieu.
 */
public class DefaultTypeConverter implements BiFunction<Class,String,Object>
{
    public Object apply(Class cls, String value) {
        if (cls==String.class){
            return value;
        }
        if (cls==Integer.class){
            return Integer.parseInt(value);
        }
        if (cls==int.class){
            return Integer.parseInt(value);
        }
        if (cls==double.class){
            return Double.parseDouble(value);
        }
        throw new RuntimeException("not implemented "+cls.getName());
    }
}