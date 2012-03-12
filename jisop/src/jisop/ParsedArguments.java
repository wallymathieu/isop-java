package jisop;

import java.io.PrintStream;
import java.util.Collection;

/**
 *
 * @author mathieu
 */
public class ParsedArguments {

    public ParsedArguments() {
    }
    /// <summary>
    /// 
    /// </summary>
    /// <param name="parsedArguments"></param>

    public ParsedArguments(ParsedArguments parsedArguments) {
        RecognizedArguments = parsedArguments.RecognizedArguments;
        ArgumentWithOptions = parsedArguments.ArgumentWithOptions;
        UnRecognizedArguments = parsedArguments.UnRecognizedArguments;
    }
    public Collection<RecognizedArgument> RecognizedArguments;
    public Collection<UnrecognizedArgument> UnRecognizedArguments;
    public Collection<ArgumentWithOptions> ArgumentWithOptions;

    public Collection<ArgumentWithOptions> UnMatchedRequiredArguments() {
        throw new RuntimeException();
        /*
         * var unMatchedRequiredArguments = ArgumentWithOptions
         * .Where(argumentWithOptions => argumentWithOptions.Required)
         * .Where(argumentWithOptions => !RecognizedArguments .Any(recogn =>
         * recogn.WithOptions.Equals(argumentWithOptions))); return unMatchedRequiredArguments;
         */
    }

    public void Invoke(PrintStream out) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
