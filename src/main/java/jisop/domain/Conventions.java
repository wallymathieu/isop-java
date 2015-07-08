package jisop.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by mathieu.
 */
public class Conventions {
    public static String ControllerName="controller";

    public static Set<String> ConfigurationName = IgnoreCase(new String[]{"isopconfiguration"});

    private static Set<String> IgnoreCase(String[] strings) {
        Set<String> s= new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        s.addAll(Arrays.asList(strings));
        return s;
    }

    public static String Help="help";

    public static String Index ="index";

    public static boolean equalsHelp(String value){
        return Help.equals(value.toLowerCase());
    }
    public static boolean equalsIndex(String value){
        return Index.equals(value.toLowerCase());
    }
}
