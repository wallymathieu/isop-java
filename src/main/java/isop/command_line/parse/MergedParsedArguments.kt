/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isop.command_line.parse

import isop.infrastructure.Lists

import java.util.stream.Stream

/**
 *
 * @author mathieu
 */
internal class MergedParsedArguments(private val _first: ParsedArguments, private val _second: ParsedArguments) : ParsedArguments(Lists.union(_first.argumentWithOptions, _second.argumentWithOptions), Lists.union(_first.recognizedArguments, _second.recognizedArguments), Lists.intersect(_first.unRecognizedArguments, _second.unRecognizedArguments)) {
    override fun invoke(): Stream<String> {
        var r = Stream.empty<String>()
        r = Stream.concat(r, _first.invoke())
        r = Stream.concat(r, _second.invoke())
        return r
    }
}
