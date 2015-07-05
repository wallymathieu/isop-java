/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop;

import java.io.OutputStream;
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
        recognizedArguments = ListUtils.union(first.recognizedArguments,second.recognizedArguments);
        argumentWithOptions = ListUtils.union(first.argumentWithOptions,second.argumentWithOptions);
        unRecognizedArguments = ListUtils.intersect(first.unRecognizedArguments,second.unRecognizedArguments);

    }
    @Override
    public void invoke(OutputStream out) {
        _first.invoke(out);
        _second.invoke(out);
    }

    
}
