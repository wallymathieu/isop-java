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
        recognizedArguments = ListUtils.Union(first.recognizedArguments,second.recognizedArguments);
        ArgumentWithOptions = ListUtils.Union(first.ArgumentWithOptions,second.ArgumentWithOptions);
        unRecognizedArguments = ListUtils.Intersect(first.unRecognizedArguments,second.unRecognizedArguments);

    }

    
}
