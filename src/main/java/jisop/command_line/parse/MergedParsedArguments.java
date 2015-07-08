/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop.command_line.parse;

import jisop.infrastructure.ListUtils;

import java.io.OutputStream;
import java.util.stream.Stream;

/**
 *
 * @author mathieu
 */
class MergedParsedArguments extends ParsedArguments {
    private final ParsedArguments _first;
    private final ParsedArguments _second;

    public MergedParsedArguments(ParsedArguments first, ParsedArguments second){
        super(
                ListUtils.union(first.argumentWithOptions,second.argumentWithOptions),
                ListUtils.union(first.recognizedArguments, second.recognizedArguments),
                ListUtils.intersect(first.unRecognizedArguments,second.unRecognizedArguments)
                );
         _first = first;
        _second = second;

    }
    @Override
    public Stream<String> invoke() {
        Stream<String> r=Stream.empty();
        r = Stream.concat(r, _first.invoke());
        r = Stream.concat(r, _second.invoke());
        return r;
    }
}
