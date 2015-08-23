/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isop.infrastructure;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author mathieu
 */
public final class Lists {
    private Lists(){}
    public static <T> Collection<T> union(Collection<T> a, Collection<T> b) {
       Set<T> set = new HashSet<>();
         
        set.addAll(a);
        set.addAll(b);
        return new ArrayList<>(set);
    }
    public static <T> List<T> intersect(T[] list1, T[] list2) {
        return intersect(Arrays.asList(list1), Arrays.asList(list2));
    }
    public static <T> List<T> intersect(Collection<T> list1, Collection<T> list2) {
        return list1.stream()
                .filter(list2::contains)
                .collect(Collectors.toList());
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
            retv.add(new KeyValuePair<>(i, list.get(i)));
        }
        return retv;
    }
}
