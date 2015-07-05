/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop.command_line.parse;

import jisop.infrastructure.ListUtils;

import java.io.OutputStream;

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
    public void invoke(OutputStream out) {
        _first.invoke(out);
        _second.invoke(out);
    }

    
}
