/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop.infrastructure;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

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
    public static <T> Collection<T> union(Collection<T> a, Collection<T> b) {
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

    public static <T> Stream<T> filterWithIndex(Collection<T> list, BiFunction<T,Integer,Boolean> filter) {
        return filterWithIndex(new ArrayList<>(list),filter);
    }

    public static <T> Stream<T> filterWithIndex(List<T> list, BiFunction<T,Integer,Boolean> filter) {
        return withIndex(list)
                .stream()
                .filter(tuple->filter.apply(tuple.value, tuple.key))
                .map(tuple->tuple.value)
                ;
    }

    public static <T> Collection<KeyValuePair<Integer,T>> withIndex(Collection<T> list) {
        return withIndex(new ArrayList<>(list));
    }
    public static <T> Collection<KeyValuePair<Integer,T>> withIndex(List<T> list) {
        List<KeyValuePair<Integer,T>> retv = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            retv.add(new KeyValuePair<Integer, T>(i, list.get(i)));
        }
        return retv;
    }
}
