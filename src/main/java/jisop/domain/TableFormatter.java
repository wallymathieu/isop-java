package jisop.domain;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by mathieu.
 */
public class TableFormatter implements Formatter {

    public Stream<String> Format(Object value) {
        if (value != null)
        {
            if (value instanceof String){
                return Stream.of((String) value);
            } else if (value.getClass().isPrimitive()){
                return Stream.of(value.toString());
            } else if (value.getClass().isArray()){
                List<Object> array= Arrays.asList((Object[])value);
                return getStringStream(array.stream());
            }  if (value instanceof Collection){
                return getStringStream(((Collection) value).stream());
            } else if (value instanceof Stream){
                return getStringStream((Stream) value);
            } else {
                return getStringStream(Stream.of(value));
            }
        }
        return Stream.empty();
    }

    private Stream<String> getStringStream(Stream<Object> value) {
        Iterator<Object> it = value.iterator();
        Field[] fields;
        if (it.hasNext()) {
            Object head = it.next();
            Class type = head.getClass();
            fields = getFields(type);

            return Stream.concat(Stream.of(header(fields), line(fields, head)),
                    streamOf(it, o -> line(fields, o)));
        }
        return Stream.empty();
    }
    private Stream<String> streamOf(Iterator<Object> it, Function<Object,String> map){
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(it,
                Spliterator.ORDERED),false).map(map);
    }

    private Field[] getFields(Class t) {
        return t.getFields();
        //return t.GetProperties(BindingFlags.Public | BindingFlags.Instance | BindingFlags.GetProperty);
    }
    private String header(Field[] fields)
    {
        return String.join("\t",
                Arrays.asList(fields)
                        .stream()
                        .map(prop -> prop.getName())
                        .collect(Collectors.toList()));
    }
    private String line(Field[] fields, Object item)
    {
        return String.join("\t",
                Arrays.asList(fields)
                        .stream()
                        .map(prop -> getValue(prop, item))
                        .collect(Collectors.toList()));
    }

    private String getValue(Field field, Object item) {
        try {
            return field.get(item).toString();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
