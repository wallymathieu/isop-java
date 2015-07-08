package jisop.command_line;

import jisop.MissingArgumentException;
import jisop.command_line.lex.ArgumentLexer;
import jisop.command_line.lex.Token;
import jisop.command_line.parse.ArgumentParameter;
import jisop.command_line.parse.ArgumentParser;
import jisop.command_line.parse.ArgumentWithOptions;
import jisop.command_line.parse.ParsedArguments;
import jisop.domain.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author mathieu
 */
public class ControllerRecognizer {

    private final boolean _allowInferParameter;
    private final Configuration _configuration;
    private final Controller _controller;
    private final Function<Class,Object> _typeContainer;

    public ControllerRecognizer(Controller controller, Configuration configuration, Function<Class, Object> typeContainer, boolean allowInferParameter) {
        this._controller = controller;
        this._configuration = configuration;
        this._typeContainer = typeContainer;
        this._allowInferParameter = allowInferParameter;
    }

    public ParsedArguments parseArgumentsAndMerge(String actionName, Map<String, String> arg, ParsedArguments parsedArguments) {
        Method methodInfo = _controller.getMethod(actionName);
        Collection<Argument> argumentRecognizers = methodInfo.GetArguments();

        ArgumentParser parser = new ArgumentParser(argumentRecognizers, _allowInferParameter);
        ParsedArguments parsedMethodArguments = parser.parse(arg);
        ParsedMethod parsedMethod = parse(methodInfo, parsedMethodArguments);
        ParsedArguments merged = parsedArguments.merge(parsedMethod);
        if (!_controller.ignoreGlobalUnMatchedParameters)
            merged.assertFailOnUnMatched();
        return merged;
    }

    public ParsedArguments parseArgumentsAndMerge(String[] arg, ParsedArguments parsedArguments) {
        ParsedArguments parsedMethod = parse(arg);
        // Inferred ordinal arguments should not be recognized twice
        parsedArguments.recognizedArguments = parsedArguments.recognizedArguments
                .stream()
                .filter(argopts ->
                !parsedMethod.recognizedArguments.stream()
                        .anyMatch(pargopt -> pargopt.index == argopts.index && argopts.inferredOrdinal))
                .collect(Collectors.toList())
        ;
        ParsedArguments merged = parsedArguments.merge(parsedMethod);
        if (!_controller.ignoreGlobalUnMatchedParameters)
            merged.assertFailOnUnMatched();
        return merged;
    }

    public boolean recognize(String[] arg) {
        List<Token> lexed = RewriteLexedTokensToSupportHelpAndIndex.rewrite(ArgumentLexer.lex(arg));
        return null != findMethodInfo(lexed);
    }

    public List<ArgumentWithOptions> getRecognizers(String method) {

        throw new NotImplementedException();
    }

    /// <summary>
    /// Note that in order to register a converter you can use:
    /// TypeDescriptor.AddAttributes(typeof(AType), new TypeConverterAttribute(typeof(ATypeConverter)));
    /// </summary>
    /// <param name="arg"></param>
    /// <returns></returns>
    public ParsedMethod parse(String[] arg) {
        List<Token> lexed = RewriteLexedTokensToSupportHelpAndIndex.rewrite(ArgumentLexer.lex(arg));

        Method methodInfo = findMethodInfo(lexed);

        List<Argument> argumentRecognizers = methodInfo.GetArguments();
        argumentRecognizers.addAll(0, Arrays.asList(new ArgumentWithOptions[]{
                new ArgumentWithOptions(ArgumentParameter.parse("#0" + _controller.name)).type(String.class).required(true),
                new ArgumentWithOptions(ArgumentParameter.parse("#1" + methodInfo.name)).required(false).type(String.class)
        }));

        ArgumentParser parser = new ArgumentParser(argumentRecognizers, _allowInferParameter);
        ParsedArguments parsedArguments = parser.parse(lexed, Arrays.asList(arg));

        return parse(methodInfo, parsedArguments);
    }

    private Method findMethodInfo(List<Token> arg) {
        if (arg.size()<2){
            return null;
        }
        boolean foundClassName = _controller.name.equalsIgnoreCase(arg.get(0).value);
        if (foundClassName)
        {
            String methodName = arg.get(1).value;
            Method methodInfo = FindMethodAmongLexedTokens.findMethod(_controller.getControllerActionMethods(), methodName, arg);
            return methodInfo;
        }
        return null;
    }

    public ParsedMethod parse(Method methodInfo, ParsedArguments parsedArguments)
    {
        Collection<Argument> unMatchedRequiredArguments = parsedArguments.UnMatchedRequiredArguments();
        if (unMatchedRequiredArguments.stream().anyMatch(a -> true))
        {
            throw new MissingArgumentException("Missing arguments",
                    unMatchedRequiredArguments.stream().map(a->a.name).toArray(size->new String[size]))
            ;
        }
        ConvertArgumentsToParameterValue convertArgument = new ConvertArgumentsToParameterValue(_configuration.typeConverter);
        Collection<Object> recognizedActionParameters = convertArgument.getParametersForMethod(methodInfo,
                parsedArguments.RecognizedArgumentsAsPairs(),
                _typeContainer);
        ParsedMethod m= new ParsedMethod( parsedArguments, _typeContainer, _configuration);
        m.recognizedAction = methodInfo;
        m.recognizedActionParameters = recognizedActionParameters.toArray();
        m.recognizedClass = _controller.type;
        return m;
    }

}
