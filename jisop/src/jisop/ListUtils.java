/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop;

import java.util.*;

/**
 *
 * @author mathieu
 */
public class ListUtils {
    private static <T> List<T> ToList(T[] a)
    {
        List<T> a1= new ArrayList();
        for (int i = 0; i < a.length; i++) {
            a1.add(a[i]);
        }
        return a1;
    }
    public static <T> List<T> union(T[] a, T[] b) {
        return union(ToList(a),ToList(b));
    }
    public static <T> List<T> union(List<T> a, List<T> b) {
       Set<T> set = new HashSet<T>();
         
        set.addAll(a);
        set.addAll(b);
        return new ArrayList<T>(set);
    }
    public static <T> List<T> intersect(T[] list1, T[] list2) {
        return intersect(ToList(list1), ToList(list2));
    }
    public static <T> List<T> intersect(Collection<T> list1, Collection<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }

}
