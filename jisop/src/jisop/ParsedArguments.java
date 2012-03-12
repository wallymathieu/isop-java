package jisop;

import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
    public ParsedArguments(List<ArgumentWithOptions> ArgumentWithOptions,
            List<RecognizedArgument> recognizedArguments,
            Collection<UnrecognizedArgument> UnRecognizedArguments) {
        this.ArgumentWithOptions= ArgumentWithOptions;
        this.recognizedArguments = recognizedArguments;
        this.UnRecognizedArguments = UnRecognizedArguments;
    }
    public ParsedArguments(ParsedArguments parsedArguments) {
        recognizedArguments = parsedArguments.recognizedArguments;
        ArgumentWithOptions = parsedArguments.ArgumentWithOptions;
        UnRecognizedArguments = parsedArguments.UnRecognizedArguments;
    }
    public List<RecognizedArgument> recognizedArguments;
    public Collection<UnrecognizedArgument> UnRecognizedArguments;
    public List<ArgumentWithOptions> ArgumentWithOptions;

    public Collection<ArgumentWithOptions> UnMatchedRequiredArguments() {
        LinkedList<ArgumentWithOptions> unmatched = new LinkedList<ArgumentWithOptions>();
        for (int i = 0; i < ArgumentWithOptions.size(); i++) {
            ArgumentWithOptions arg = ArgumentWithOptions.get(i);
            if (arg.Required && !isRecognized(arg))
                unmatched.add(arg);
        }
        return unmatched;
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

    private boolean isRecognized(jisop.ArgumentWithOptions arg) {
        for (int i = 0; i < recognizedArguments.size(); i++) {
            if (recognizedArguments.get(i).withOptions.equals(arg))
                return true;
        }
        return false;
    }
}
