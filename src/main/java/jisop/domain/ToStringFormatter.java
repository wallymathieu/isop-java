package jisop.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by mathieu.
 */
public class ToStringFormatter implements Formatter {
    @Override
    public Stream<String> format(Object value) {
        if (value != null){
            if (value instanceof String){
                return Stream.of((String) value);
            }
            if (value.getClass().isPrimitive()){
                return Stream.of(value.toString());
            }
            if (value.getClass().isArray()){
                return toStrings(Arrays.asList((Object[]) value).stream());
            }
            if (value instanceof Collection){
                return toStrings(((Collection) value).stream());
            }
            if (value instanceof Stream){
                return toStrings((Stream) value);
            }
            return Stream.of(value.toString());
       }
        return Stream.empty();
    }

    private Stream toStrings(Stream value) {
        return value.map(Object::toString);
    }
}
