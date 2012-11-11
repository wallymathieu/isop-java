package jisop;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author mathieu
 */
public class Build {

    private final List<ArgumentWithOptions> _argumentRecognizers;
    private final List<ControllerRecognizer> _controllerRecognizers;
    private TypeConverter typeConverter;
    private final TypeContainer _container = new TypeContainer();
    public Build() {
        _controllerRecognizers = new LinkedList<ControllerRecognizer>();
        _argumentRecognizers = new LinkedList<ArgumentWithOptions>();
    }


    public Build parameter(String argument) {
        return parameter(argument, false, null, null);
    }

    public Build parameter(String argument, boolean required) {
        return parameter(argument, required, null, null);
    }

    public Build parameter(String argument,
            boolean required,
            String description,
            ArgumentAction action) {
        _argumentRecognizers.add(new ArgumentWithOptions(ArgumentParameter.parse(argument),
                required, description, action));
        return this;
    }

    public ParsedArguments parse(String[] arg) {
        ArgumentParser argumentParser = new ArgumentParser(_argumentRecognizers);
        // TODO: Need to figure out where this goes. To Much logic for this layer.
        ArgumentLexer lexer = ArgumentLexer.lex(arg);
        ParsedArguments parsedArguments = argumentParser.Parse(lexer, arg);
        if (_controllerRecognizers.size() > 0) {
            ControllerRecognizer controllerRecognizer =
                    controllerRecognizes(arg);
            if (null != controllerRecognizer) {
                ParsedMethod parsedMethod = controllerRecognizer.parse(arg);
                parsedMethod.factory = _container;
                ParsedArguments merged = parsedArguments.merge(parsedMethod);
                if (!controllerRecognizer.ignoreGlobalUnMatchedParameters) {
                    FailOnUnMatched(merged);
                }
                return merged;
            }
        }
        FailOnUnMatched(parsedArguments);
        return parsedArguments;
    }

    private static void FailOnUnMatched(ParsedArguments parsedArguments) { // This does not belong here. This is just supposed to be a fluent layer.
        Collection<ArgumentWithOptions> unMatchedRequiredArguments = parsedArguments.UnMatchedRequiredArguments();

        if (unMatchedRequiredArguments.size() > 0) {
            throw new MissingArgumentException() /*
                     * {
                     * Arguments = unMatchedRequiredArguments .Select( unmatched
                     * => new KeyValuePair<string,
                     * string>(unmatched.Argument.ToString(),
                     * unmatched.Argument.Help())) .ToList() }
                     */;
        }
    }

    public Build RecognizeClass(Class arg) {
        _controllerRecognizers.add(new ControllerRecognizer(arg,_container));
        return this;
    }

    public Build Recognize(Object arg) {
        _controllerRecognizers.add(new ControllerRecognizer(arg.getClass(),_container));
        _container.instances.put(arg.getClass(), arg);
        return this;
    }

    public String help() {
        throw new RuntimeException("Not implemented");
        /*
         * var cout = new StringWriter(Culture); Parse(new
         * []{"Help"}).Invoke(cout); return cout.ToString();
         *
         */
    }

    public Collection<ControllerRecognizer> GetControllerRecognizers() {
        return _controllerRecognizers;
    }

    public Collection<ArgumentWithOptions> GetGlobalParameters() {
        return _argumentRecognizers;
    }
    /*
     * public Func<Type, object> GetFactory() { return
     * _container.CreateInstance; }
     */

    public Build setFactory(ObjectFactory objectFactory) {
        _container.setFactory(objectFactory);
        return this;
    }

    private ControllerRecognizer controllerRecognizes(String[] arg) {
        for (int i = 0; i < _controllerRecognizers.size(); i++) {
            ControllerRecognizer r = _controllerRecognizers.get(i);
            if (r.recognize(arg)) {
                return r;
            }
        }
        return null;
    }
}
