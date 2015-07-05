package jisop;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mathieu
 */
public class StringUtils {

    static String join(String delim, String[] array) {
        StringBuilder sb = new StringBuilder();
        if (array.length==0) return "";
        sb.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            sb.append(delim);
            sb.append(array[i]);
        }
        return sb.toString();
    }

    static boolean isNullOrEmpty(String val) {
        if (null==val)return true;
        if (val.length()==0) return true;
        return false;
    }

    static boolean containsStringIgnoreCase(String[] list, String value) {
        for (int i = 0; i < list.length; i++) {
            if (list[i].equalsIgnoreCase(value))
                return true;
        }
        return false;
    }

    static String trimEnd(String str, String stripChars) {
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
