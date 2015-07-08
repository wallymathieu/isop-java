package jisop.infrastructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author mathieu
 */
public class StringUtils {
    public static String join(String delim, Stream<String> array) {
        return array.collect(Collectors.joining(delim));
    }
    public static String join(String delim, String[] array) {
        return join(delim, Arrays.asList(array).stream());
    }

    public static boolean isNullOrEmpty(String val) {
        if (null==val)return true;
        if (val.length()==0) return true;
        return false;
    }

    public static boolean containsStringIgnoreCase(String[] list, String value) {
        for (int i = 0; i < list.length; i++) {
            if (list[i].equalsIgnoreCase(value))
                return true;
        }
        return false;
    }

    public static String trimEnd(String str, String stripChars) {
       int end;
        if (str == null || (end = str.length()) == 0) {
            return str;
        }
        while (end != 0 && stripChars.indexOf(str.charAt(end - 1))>=0 ) {
            end--;
        }
        return str.substring(0, end);
    }

    public static String[] split(String str, char val) {
        List<String> retval = new ArrayList<String>();
        int last = -1;
        
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i)==val){
                retval.add(str.substring(last+1,i));
                last = i;
            }
        }
        if (last<str.length()){
            retval.add(str.substring(last+1,str.length()));
        }
        return retval.toArray(new String[0]);
    }
    
}
