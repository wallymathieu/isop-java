package jisop;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author mathieu
 */
public class Build {

     private final Collection<ArgumentWithOptions> _argumentRecognizers;
     private final Collection<ControllerRecognizer> _controllerRecognizers;
     //public TypeConverterFunc TypeConverter { get; private set; }
        private final TypeContainer _container=new TypeContainer();
        
        public Build()
        {
            _controllerRecognizers = new LinkedList<ControllerRecognizer>();
            _argumentRecognizers = new LinkedList<ArgumentWithOptions>();
        }
public Build parameter(String argument)
{
    return Parameter(argument,false,null);
}
        
        public Build Parameter(String argument, boolean required, String description)
        {
            _argumentRecognizers.add(new ArgumentWithOptions(ArgumentParameter.parse(argument), 
                    required, description));
            return this;
        }
		
        public ParsedArguments parse(String[] arg)
        {
            ArgumentParser argumentParser = new ArgumentParser(_argumentRecognizers);
            // TODO: Need to figure out where this goes. To Much logic for this layer.
            ArgumentLexer lexer = ArgumentLexer.lex(arg);
            ParsedArguments parsedArguments = argumentParser.Parse(lexer, arg);
            if (_controllerRecognizers.size()>0)
            {
                throw new RuntimeException("not implemented");
                /*
                var controllerRecognizer = 
                        _controllerRecognizers.FirstOrDefault(recognizer => recognizer.Recognize(arg));
                if (null != controllerRecognizer)
                {
					var parsedMethod = controllerRecognizer.Parse(arg);
					parsedMethod.Factory = _container.CreateInstance;
                    var merged = parsedArguments.Merge( parsedMethod);
                    if (!controllerRecognizer.IgnoreGlobalUnMatchedParameters)
                        FailOnUnMatched(merged);
                    return merged;
                }*/
            }
            FailOnUnMatched(parsedArguments);
            return parsedArguments;
        }

        private static void FailOnUnMatched(ParsedArguments parsedArguments)
        { // This does not belong here. This is just supposed to be a fluent layer.
            Collection<ArgumentWithOptions> unMatchedRequiredArguments = parsedArguments.UnMatchedRequiredArguments();

            if (unMatchedRequiredArguments.size()>0)
            {
                throw new RuntimeException("Missing arguments")
                          /*{
                              Arguments = unMatchedRequiredArguments
                                  .Select(
                                      unmatched =>
                                      new KeyValuePair<string, string>(unmatched.Argument.ToString(), unmatched.Argument.Help()))
                                  .ToList()
                          }*/;
            }
        }

        public Build RecognizeClass(Class arg)
        {
            _controllerRecognizers.add(new ControllerRecognizer(arg));
            return this;
        }
        public Build Recognize(Object arg)
        {
            _controllerRecognizers.add(new ControllerRecognizer(arg.getClass()));
            _container.instances.put(arg.getClass(),arg);
            return this;
        }

        public String help()
        {
            throw new RuntimeException("Not implemented");
            /*
            var cout = new StringWriter(Culture);
            Parse(new []{"Help"}).Invoke(cout);
			return cout.ToString();
                       
                        */
        }

        public Collection<ControllerRecognizer> GetControllerRecognizers()
        {
            return _controllerRecognizers; 
        }

        public Collection<ArgumentWithOptions> GetGlobalParameters()
        {
            return _argumentRecognizers; 
        }
/*
        public Func<Type, object> GetFactory()
        {
            return _container.CreateInstance;
        }*/
    
}
