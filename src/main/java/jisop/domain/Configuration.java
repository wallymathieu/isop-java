package jisop.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by mathieu.
 */
public class Configuration {
    public Configuration()
    {
        recognizes = new ArrayList<>();
        fields = new ArrayList<>();
        formatter = new ToStringFormatter();
        typeConverter = new DefaultTypeConverter();
    }
    public final List<Controller> recognizes;
    public final List<Property> fields;
    public Function<Class, Object> factory;
    public BiFunction<Class,String,Object> typeConverter;
    public boolean recognizesHelp;
    public Formatter formatter;
}
