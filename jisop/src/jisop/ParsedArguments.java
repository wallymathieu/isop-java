package jisop;

import java.io.OutputStream;
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
        this.argumentWithOptions= ArgumentWithOptions;
        this.recognizedArguments = recognizedArguments;
        this.unRecognizedArguments = UnRecognizedArguments;
    }
    public ParsedArguments(ParsedArguments parsedArguments) {
        recognizedArguments = parsedArguments.recognizedArguments;
        argumentWithOptions = parsedArguments.argumentWithOptions;
        unRecognizedArguments = parsedArguments.unRecognizedArguments;
    }
    public List<RecognizedArgument> recognizedArguments;
    public Collection<UnrecognizedArgument> unRecognizedArguments;
    public List<ArgumentWithOptions> argumentWithOptions;

    public Collection<ArgumentWithOptions> UnMatchedRequiredArguments() {
        LinkedList<ArgumentWithOptions> unmatched = new LinkedList<ArgumentWithOptions>();
        for (int i = 0; i < argumentWithOptions.size(); i++) {
            ArgumentWithOptions arg = argumentWithOptions.get(i);
            if (arg.Required && !isRecognized(arg))
                unmatched.add(arg);
        }
        return unmatched;
    }

    public void invoke(OutputStream out) {
        for (int i = 0; i < recognizedArguments.size(); i++) {
            RecognizedArgument arg= recognizedArguments.get(i);
            if (null!=arg.withOptions.action){
                arg.withOptions.action.invoke(arg.value);
            }
        }
    }

    private boolean isRecognized(jisop.ArgumentWithOptions arg) {
        for (int i = 0; i < recognizedArguments.size(); i++) {
            if (recognizedArguments.get(i).withOptions.equals(arg))
                return true;
        }
        return false;
    }

    ParsedArguments merge(ParsedArguments parsedMethod) {
        return new MergedParsedArguments(this, parsedMethod);
    }

    public RecognizedArgument withName(String name) {
        for (RecognizedArgument r: recognizedArguments) {
            if (r.argument.toUpperCase().equals(name.toUpperCase()))
                return r;
        }
        return null;
    }
}
