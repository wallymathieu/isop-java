package jisop;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author mathieu
 */
public class ArgumentParser {

    private final List<ArgumentWithOptions> _argumentWithOptions;

    public ArgumentParser(List<ArgumentWithOptions> argumentWithOptions) {
        _argumentWithOptions = argumentWithOptions;
    }

    private ArgumentWithOptions argumentWithOptionsThatAccepts(Integer index, String value) {
        // var argumentWithOptions = _argumentWithOptions
        //                       .SingleOrDefault(argopt => argopt.Argument.Accept(current.Index,current.Value));
        for (int i = 0; i < _argumentWithOptions.size(); i++) {
            ArgumentWithOptions arg =_argumentWithOptions.get(i);
            if (arg.argument.accept(index, value)) {
                return arg;
            }
        }
        return null;
    }

    private Collection<UnrecognizedArgument> unrecoqnizedArguments(String[] arguments, LinkedList<Integer> recognizedIndexes) {
        LinkedList<UnrecognizedArgument> unrecognized = new LinkedList<UnrecognizedArgument>();
        for (int i = 0; i < arguments.length; i++) {
            if (!recognizedIndexes.contains(new Integer(i)))
                unrecognized.add(new UnrecognizedArgument(i,arguments[i]));
        }
        return unrecognized;
//        .Select((value, i) => 
        //               new {i, value }) .Where(indexAndValue =>
        //                !recognizedIndexes.Contains(indexAndValue.i)) .Select(v => new
        //    UnrecognizedArgument { Index = v.i, Value = v.value })
    }

    public ParsedArguments Parse(String[] arguments) {
        ArgumentLexer lexer = ArgumentLexer.lex(arguments);
        ParsedArguments parsedArguments = Parse(lexer, arguments);
        Collection<ArgumentWithOptions> unMatchedRequiredArguments = parsedArguments.UnMatchedRequiredArguments();

        if (unMatchedRequiredArguments.size() > 0) {
            throw new RuntimeException("Missing arguments") {
                /*
                 * Arguments = unMatchedRequiredArguments .Select(unmatched =>
                 * new KeyValuePair<string,
                 * string>(unmatched.Argument.ToString(),
                 * unmatched.Argument.Help())).ToList()
                 */
            };
        }
        return parsedArguments;
    }

    public ParsedArguments Parse(ArgumentLexer lex, String[] arguments) {
        LinkedList<Integer> recognizedIndexes = new LinkedList<Integer>();
        PeekCollection<Token> lexer = new PeekCollection<Token>(lex);
        LinkedList<RecognizedArgument> recognized = new LinkedList<RecognizedArgument>();
        while (lexer.hasMore()) {
            Token current = lexer.next();
            switch (current.TokenType) {
                case Argument: {
                    ArgumentWithOptions argumentWithOptions =
                            argumentWithOptionsThatAccepts(current.Index, current.Value);
                    if (null == argumentWithOptions) {
                        continue;
                    } else {

                        recognizedIndexes.add(current.Index);
                        recognized.add(new RecognizedArgument(argumentWithOptions, current.Value));
                    }
                }
                break;
                case Parameter: {
                    ArgumentWithOptions argumentWithOptions =
                            argumentWithOptionsThatAccepts(current.Index, current.Value);
                    if (null == argumentWithOptions) {
                        continue;
                    } else {
                        String value;
                        recognizedIndexes.add(current.Index);
                        if (lexer.peekNext() != null && lexer.peekNext().TokenType == TokenType.ParameterValue) {
                            Token paramValue = lexer.next();
                            recognizedIndexes.add(paramValue.Index);
                            value = paramValue.Value;
                        } else {
                            value = "";
                        }

                        recognized.add(new RecognizedArgument(argumentWithOptions, current.Value, value));
                    }
                }
                break;
                case ParameterValue:
                    break;
                default:
                    throw new RuntimeException(current.TokenType.toString());
            }
        }


        Collection<UnrecognizedArgument> unRecognizedArguments = unrecoqnizedArguments(arguments, recognizedIndexes);

        return new ParsedArguments(_argumentWithOptions,
                recognized,
                unRecognizedArguments);
    }
}
