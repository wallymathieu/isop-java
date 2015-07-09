package jisop.domain;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by mathieu.
 */
public class ToStringFormatter implements Formatter {
    @Override
    public Stream<String> format(Object value) {
        Stream<String> r= Stream.empty();
        if (value != null)
        {
            if (value instanceof String){
                r = Stream.concat(r, Stream.of((String) value));
            }else if (value.getClass().isPrimitive()){
                r = Stream.concat(r, Stream.of(value.toString()));
            }else if (value instanceof Collection){
                r = Stream.concat(r, toStrings(((Collection) value).stream()));
            }else if (value instanceof Stream){
                r = Stream.concat(r, toStrings((Stream) value));
            }else{
                r = Stream.concat(r, Stream.of(value.toString()));
            }
        }
        return r;
    }

    private Stream toStrings(Stream value) {
        return value
                .map(v -> v.toString());
    }
}
