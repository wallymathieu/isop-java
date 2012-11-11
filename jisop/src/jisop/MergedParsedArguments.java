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
class MergedParsedArguments extends ParsedArguments {
    private final ParsedArguments _first;
    private final ParsedArguments _second;

    public MergedParsedArguments(ParsedArguments first, ParsedArguments second) {
         _first = first;
        _second = second;
        recognizedArguments = Union(first.recognizedArguments,second.recognizedArguments);
        ArgumentWithOptions = Union(first.ArgumentWithOptions,second.ArgumentWithOptions);
        unRecognizedArguments = Intersect(first.unRecognizedArguments,second.unRecognizedArguments);

    }

    private <T> List<T> Union(List<T> a, List<T> b) {
       Set<T> set = new HashSet<T>();
         
        set.addAll(a);
        set.addAll(b);
        return new ArrayList<T>(set);
    }

    private <T> List<T> Intersect(Collection<T> list1, Collection<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
    
}
