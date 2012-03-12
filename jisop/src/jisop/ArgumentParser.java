package jisop;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author mathieu
 */
public class ArgumentParser {
      private final Collection<ArgumentWithOptions> _argumentWithOptions;

        public ArgumentParser(Collection<ArgumentWithOptions> argumentWithOptions)
        {
            _argumentWithOptions = argumentWithOptions;
        }
        public ParsedArguments Parse(String[] arguments)
        {
            ArgumentLexer lexer = ArgumentLexer.lex(arguments);
            ParsedArguments parsedArguments = Parse(lexer, arguments);
            Collection<ArgumentWithOptions> unMatchedRequiredArguments = parsedArguments.UnMatchedRequiredArguments();

            if (unMatchedRequiredArguments.size()>0)
            {
                throw new RuntimeException("Missing arguments")
                {
                    /*
                    Arguments = unMatchedRequiredArguments
                        .Select(unmatched => new KeyValuePair<string, string>(unmatched.Argument.ToString(), unmatched.Argument.Help())).ToList()
                        */
                };
            }
            return parsedArguments;
        }

        public ParsedArguments Parse(ArgumentLexer lex, String[] arguments)
        {
            LinkedList<Integer> recognizedIndexes = new LinkedList<Integer>();
            PeekCollection<Token> lexer = new PeekCollection<Token>(lex);
            LinkedList<RecognizedArgument> recognized = new LinkedList<RecognizedArgument>();
            while (lexer.hasMore())
            {
                Token current = lexer.next();
                switch (current.TokenType)
                {
                    case Argument:
                        {
                            
                         /*   var argumentWithOptions = _argumentWithOptions
                               .SingleOrDefault(argopt => argopt.Argument.Accept(current.Index,current.Value));
                            
                            if (null == argumentWithOptions)
                            {
                                    continue;
                            }
                            
                            recognizedIndexes.Add(current.Index);
                            recognized.Add(new RecognizedArgument(
                                        argumentWithOptions,
                                        current.Value));
                                        
                                        */
                            throw new RuntimeException("not implemented");
                        }
                        //break;
                    case Parameter:
                        {
                         /*   var argumentWithOptions = _argumentWithOptions
                               .SingleOrDefault(argopt => argopt.Argument.Accept(current.Index,current.Value));
                            if (null == argumentWithOptions)
                                continue;
                            string value;
                            recognizedIndexes.Add(current.Index);
                            if (lexer.Peek().TokenType == TokenType.ParameterValue)
                            {
                                var paramValue = lexer.Next();
                                recognizedIndexes.Add(paramValue.Index);
                                value = paramValue.Value;
                            }
                            else
                            {
                                value = string.Empty;
                            }

                            recognized.Add(new RecognizedArgument(
                                        argumentWithOptions,
                                        current.Value,
                                        value));
                                        
                                        */
                            throw new RuntimeException("not implemented");
                        }
                        //break;
                    case ParameterValue:
                        break;
                    default:
                        throw new RuntimeException(current.TokenType.toString());
                }
            }

            /*var argumentList = arguments.toList();

            var unRecognizedArguments = argumentList
                .Select((value, i) => new { i, value })
                .Where(indexAndValue => !recognizedIndexes.Contains(indexAndValue.i))
                .Select(v => new UnrecognizedArgument { Index = v.i, Value = v.value });

            return new ParsedArguments
            {
                ArgumentWithOptions = _argumentWithOptions.ToArray(),
                RecognizedArguments = recognized,
                UnRecognizedArguments = unRecognizedArguments
            };
            
            */
            throw new RuntimeException();
        }
}
