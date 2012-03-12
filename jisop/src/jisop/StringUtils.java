/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop;

/**
 *
 * @author mathieu
 */
class StringUtils {

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
    
}
