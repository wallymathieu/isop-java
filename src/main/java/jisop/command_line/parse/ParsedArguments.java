package jisop.command_line.parse;

import jisop.MissingArgumentException;
import jisop.domain.Argument;
import jisop.infrastructure.KeyValuePair;

import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author mathieu
 */
public class ParsedArguments {

    /// <summary>
    /// 
    /// </summary>
    /// <param name="parsedArguments"></param>
    public ParsedArguments(Collection<Argument> ArgumentWithOptions,
            Collection<RecognizedArgument> recognizedArguments,
            Collection<UnrecognizedArgument> UnRecognizedArguments) {
        this.argumentWithOptions= ArgumentWithOptions;
        this.recognizedArguments = recognizedArguments;
        this.unRecognizedArguments = UnRecognizedArguments;
    }
    public ParsedArguments(ParsedArguments parsedArguments) {
        recognizedArguments = parsedArguments.recognizedArguments;
        argumentWithOptions = parsedArguments.argumentWithOptions;
        unRecognizedArguments = parsedArguments.unRecognizedArguments;
    }
    public Collection<RecognizedArgument> recognizedArguments;
    public Collection<UnrecognizedArgument> unRecognizedArguments;
    public final Collection<Argument> argumentWithOptions;

    public Collection<Argument> UnMatchedRequiredArguments() {
        return argumentWithOptions.stream()
                .filter(arg->arg.required && !isRecognized(arg))
                .collect(Collectors.toList());
    }

    public Stream<String> invoke() {
        recognizedArguments.stream()
                .filter(arg -> null != arg.argument.action)
                .forEach(arg -> arg.argument.action.accept(arg.value));
        return Stream.of("");
    }

    private boolean isRecognized(Argument arg) {
        return recognizedArguments.stream()
                .anyMatch(a->a.argument.equals(arg));
    }

    public ParsedArguments merge(ParsedArguments parsedMethod) {
        return new MergedParsedArguments(this, parsedMethod);
    }

    public void assertFailOnUnMatched() {
        Collection<Argument> unMatchedRequiredArguments = UnMatchedRequiredArguments();

        if (unMatchedRequiredArguments.size() > 0) {
            throw new MissingArgumentException("Missing arguments",
                    unMatchedRequiredArguments.stream().map(a->a.name).toArray(size->new String[size]));
        }
    }

    public Collection<KeyValuePair<String, String>> RecognizedArgumentsAsPairs() {
        return recognizedArguments.stream()
                .map(r-> r.asKeyValue())
                .collect(Collectors.toList());
    }
}
