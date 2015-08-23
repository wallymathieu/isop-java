package isop.command_line.parse;

import isop.command_line.lex.Token;
import isop.command_line.lex.TokenType;
import isop.domain.Argument;
import isop.infrastructure.ArgumentOutOfRangeException;
import isop.infrastructure.Lists;
import isop.infrastructure.PeekCollection;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author mathieu
 */
public class ArgumentParser {

    private final Collection<Argument> _argumentWithOptions;
    private final boolean _allowInferParameter;

    public ArgumentParser(Collection<Argument> argumentWithOptions, boolean allowInferParameter) {
        _argumentWithOptions = argumentWithOptions;
        _allowInferParameter = allowInferParameter;
    }

    public ParsedArguments parse(Map<String, String> arg){
        List<RecognizedArgument> recognized = new ArrayList<>();
        List<UnrecognizedArgument> unRecognizedArguments = new ArrayList<>();
        int index=0;
        for(String key: arg.keySet()){
            Argument argumentWithOptions = _argumentWithOptions
                    .stream()
                    .filter(argopt->accept(argopt,key))
                    .findFirst()
                    .orElse(null);


            if (null == argumentWithOptions)
            {
                unRecognizedArguments.add(new UnrecognizedArgument(index++,key));
                continue;
            }
            recognized.add(new RecognizedArgument(
                    argumentWithOptions,
                    index++,
                    key,
                    arg.get(key)));

        }
        return new ParsedArguments(_argumentWithOptions,recognized,unRecognizedArguments);
    }
    public ParsedArguments parse(List<Token> lexed, Collection<String> arguments)
    {
        List<Integer> recognizedIndexes = new ArrayList<>();
        PeekCollection<Token> peekTokens = new PeekCollection<>(lexed);
        boolean encounteredParameter = false;
        List<RecognizedArgument> recognized = new ArrayList<>();
        while (peekTokens.hasMore())
        {
            Token current = peekTokens.next();
            switch (current.tokenType)
            {
                case ARGUMENT:
                {
                    Argument argumentWithOptions = _argumentWithOptions
                            .stream()
                            .filter(argopt -> accept(argopt, current.index, current.value))
                            .findFirst()
                            .orElse(null);

                    if (null == argumentWithOptions && !encounteredParameter && _allowInferParameter)
                    {
                        inferParameter(recognizedIndexes, recognized, current);
                        continue;
                    }

                    if (null == argumentWithOptions)
                    {
                        continue;
                    }

                    recognizedIndexes.add(current.index);
                    recognized.add(new RecognizedArgument(
                            argumentWithOptions,
                            current.index,
                            current.value));
                }
                break;
                case PARAMETER:
                {
                    encounteredParameter = true;
                    Argument argumentWithOptions = _argumentWithOptions
                            .stream()
                            .filter(argopt->accept(argopt,current.index,current.value))
                            .findFirst()
                            .orElse(null)
                            ;
                    if (null == argumentWithOptions)
                        continue;
                    String value;
                    recognizedIndexes.add(current.index);
                    Token next = peekTokens.peek();
                    if (null!=next && next.tokenType.equals( TokenType.PARAMETER_VALUE))
                    {
                        Token paramValue = peekTokens.next();
                        recognizedIndexes.add(paramValue.index);
                        value = paramValue.value;
                    }
                    else
                    {
                        value = "";
                    }

                    recognized.add(new RecognizedArgument(
                            argumentWithOptions,
                            current.index,
                            current.value,
                            value));
                }
                break;
                case PARAMETER_VALUE:
                    break;
                default:
                    throw new ArgumentOutOfRangeException(current.tokenType.toString());
            }
        }

        List<String> argumentList = arguments
                .stream()
                .collect(Collectors.toList());

        List<UnrecognizedArgument> unRecognizedArguments = Lists.withIndex(argumentList)
                .stream()
                .filter(tpl->!recognizedIndexes.contains(tpl.key))
                .map(tpl -> new UnrecognizedArgument(tpl.key, tpl.value))
                .collect(Collectors.toList());
        return new ParsedArguments(_argumentWithOptions,recognized,unRecognizedArguments);
    }

    private void inferParameter(List<Integer> recognizedIndexes, List<RecognizedArgument> recognized, Token current) {
        Argument argumentWithOptions;
        argumentWithOptions = Lists
                .filterWithIndex(_argumentWithOptions, (argOpts, i) -> i.equals(current.index))
                .findFirst()
                .orElse(null)
        ;
        if (null != argumentWithOptions)
        {
            recognizedIndexes.add(current.index);
            RecognizedArgument arg = new RecognizedArgument(
                    argumentWithOptions,
                    current.index,
                    argumentWithOptions.name,
                    current.value);
            arg.inferredOrdinal = true;
            recognized.add(arg);
        }
    }

    private boolean accept(Argument argument, int index, String value) {

        if (argument instanceof ArgumentWithOptions)
        {
            return ((ArgumentWithOptions)argument).argument.accept(index, value);
        }
        return ArgumentParameter.parse(argument.name).accept(index, value);
    }

    private boolean accept(Argument argument, String value) {

        if (argument instanceof ArgumentWithOptions)
        {
            return ((ArgumentWithOptions)argument).argument.accept(value);
        }
        return ArgumentParameter.parse(argument.name).accept(value);
    }

}
