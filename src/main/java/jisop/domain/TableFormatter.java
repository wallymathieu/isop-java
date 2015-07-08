package jisop.domain;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mathieu.
 */
public class TableFormatter implements Formatter {

    public Stream<String> Format(Object value) {
        Stream<String> r= Stream.empty();
        if (value != null)
        {
            if (value instanceof String){
                r = Stream.concat(r, Stream.of((String) value));
            } else if (value.getClass().isPrimitive()){
                r = Stream.concat(r, Stream.of(value.toString()));
            } else if (value instanceof Collection){
                r = Stream.concat(r, getStringStream(((Collection) value).stream()));
            } else  if (value instanceof Stream){
                r = Stream.concat(r, getStringStream((Stream) value));
            } else {
                r = Stream.concat(r, getStringStream(Stream.of(value)));
            }
        }
        return r;
    }

    private Stream<String> getStringStream(Stream value) {
        Type if1 = Arrays.asList(value.getClass().getGenericInterfaces())
                .stream()
                .filter((Type iff) -> isCollectionType((Class) iff))
                .findFirst()
                .orElse(null);
        Type type = if1.getClass().getGenericInterfaces()[0];
        Field[] properties = getFields((Class) type);
        Stream r = Stream.of(header(properties));
        r = Stream.concat(r, value.map(item -> line(properties, item)));
        return r;
    }

    private Field[] getFields(Class t)
    {
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

    private boolean isCollectionType(Class iff) {
        return Collection.class.isAssignableFrom(iff);
    }
}
