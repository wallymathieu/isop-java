package jisop.command_line.parse;

import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author mathieu
 */
public class ParsedArguments {

    /// <summary>
    /// 
    /// </summary>
    /// <param name="parsedArguments"></param>
    public ParsedArguments(Collection<ArgumentWithOptions> ArgumentWithOptions,
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
    public final Collection<RecognizedArgument> recognizedArguments;
    public Collection<UnrecognizedArgument> unRecognizedArguments;
    public final Collection<ArgumentWithOptions> argumentWithOptions;

    public Collection<ArgumentWithOptions> UnMatchedRequiredArguments() {
        return argumentWithOptions.stream()
                .filter(arg->arg.Required && !isRecognized(arg))
                .collect(Collectors.toList());
    }

    public void invoke(OutputStream out) {
        recognizedArguments.stream()
                .filter(arg -> null != arg.withOptions.action)
                .forEach(arg -> arg.withOptions.action.accept(arg.value));
    }

    private boolean isRecognized(ArgumentWithOptions arg) {
        return recognizedArguments.stream()
                .anyMatch(a->a.withOptions.equals(arg));
    }

    public ParsedArguments merge(ParsedArguments parsedMethod) {
        return new MergedParsedArguments(this, parsedMethod);
    }

    public RecognizedArgument withName(String name) {
        return recognizedArguments
                .stream()
                .filter(r->r.argument.toUpperCase().equals(name.toUpperCase()))
                .findFirst()
                .get();
    }

    public void withoutIndex0to1() {
        unRecognizedArguments =
                unRecognizedArguments.stream()
                        .filter(arg->arg.index>=2)
                        .collect(Collectors.toList());
    }
}
