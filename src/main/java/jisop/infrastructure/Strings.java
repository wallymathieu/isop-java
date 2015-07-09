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
public final class Strings {
    private Strings(){}
    public static String join(String delimiter, Stream<String> array) {
        return array.collect(Collectors.joining(delimiter)).trim();
    }
    public static String join(String delimiter, String[] array) {
        return join(delimiter, Arrays.asList(array).stream());
    }

    public static boolean isNullOrEmpty(String val) {
        return null == val || val.length() == 0;
    }

    public static boolean containsStringIgnoreCase(String[] list, String value) {
        return Arrays.asList(list).stream()
                .anyMatch(al->al.equalsIgnoreCase(value));
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
        List<String> retval = new ArrayList<>();
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
        return retval.toArray(new String[retval.size()]);
    }
    
}
