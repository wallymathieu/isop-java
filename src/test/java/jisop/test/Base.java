package jisop.test;

import java.util.AbstractMap;
import java.util.function.Function;
import java.util.regex.*;
import java.util.*;

/**
 * Created by mathieu.
 */
public class Base {
    protected String toString(Object o){
        return o.toString();
    }
    private static Pattern toKv= Pattern.compile("\\[(#?\\w*), (\\w*)\\]");
    protected static List<AbstractMap.SimpleEntry<String, Object>> dictionaryDescriptionToKv(String input, Function<String, Object> convert)
    {
        List<AbstractMap.SimpleEntry<String,Object>> retval= new ArrayList<>();
        Matcher m = toKv.matcher(input);
        while(m.find()){
            retval.add(toSimpleEntry( m.group(1), convert.apply( m.group(2))));
        }
        return retval;
    }
    protected static AbstractMap.SimpleEntry<String,Object> toSimpleEntry(String key, Object value){
        return new AbstractMap.SimpleEntry<>(key,value);
    }
}
