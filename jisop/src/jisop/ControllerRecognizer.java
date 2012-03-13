package jisop;

import java.lang.reflect.Method;
import java.util.*;

/**
 *
 * @author mathieu
 */
class ControllerRecognizer {

    private Class type;
    boolean ignoreGlobalUnMatchedParameters = false;
    Transform _transform = new Transform();
    private TypeConverter _typeConverter;

    ControllerRecognizer(Class arg) {
        type = arg;
    }

    boolean recognize(String[] arg) {
        ArgumentLexer lexer = _transform.Rewrite(ArgumentLexer.lex(arg));
        return null != FindMethodInfo(lexer);
    }

    private List<ArgumentWithOptions> GetRecognizers(Method method) {
        throw new RuntimeException("Not implemented");
        //  var parameterInfos = method.GetParameters();
        //  var recognizers = parameterInfos
        //      .Select (parameterInfo => 
        //          new ArgumentWithOptions (ArgumentParameter.Parse (parameterInfo.Name,_culture), required: true))
        //      .ToList ();
        //  recognizers.Insert(0, new ArgumentWithOptions(ArgumentParameter.Parse("#1" + method.Name, _culture), required: false));
        //  return recognizers;
        
    }

    ParsedMethod parse(String[] arg) {
        ArgumentLexer lexer = _transform.Rewrite(ArgumentLexer.lex(arg));

        Method methodInfo = FindMethodInfo(lexer);

        List<ArgumentWithOptions> argumentRecognizers = GetRecognizers(methodInfo);

        ArgumentParser parser = new ArgumentParser(argumentRecognizers);
        ParsedArguments parsedArguments = parser.Parse(lexer, arg);

        return Parse(methodInfo, parsedArguments);
    }

    private static List<Object> GetParametersForMethod(Method method, ParsedArguments parsedArguments) {
        throw new RuntimeException("Not implemented");
        
        //var parameterInfos = method.getParameterTypes();
        //var parameters = new List<Object>();

        //foreach (var paramInfo in parameterInfos)
        //{
        //    var recognizedArgument =  parsedArguments.RecognizedArguments.First(
        //        a => a.Argument.ToUpperInvariant().Equals(paramInfo.Name.ToUpperInvariant()));
        //    parameters.Add( ConvertFrom (recognizedArgument, paramInfo));
        //}
        //return parameters;
    }

    public ParsedMethod Parse(Method methodInfo, ParsedArguments parsedArguments) {
        Collection<ArgumentWithOptions> unMatchedRequiredArguments = parsedArguments.UnMatchedRequiredArguments();
        if (unMatchedRequiredArguments.size() > 0) {
            throw new MissingArgumentException();
//                          {
//                              Arguments = unMatchedRequiredArguments
//                                  .Select(unmatched => new KeyValuePair<string, string>(unmatched.Argument.ToString(), unmatched.Argument.Help())).ToList()
//                          };
        }

        List<Object> recognizedActionParameters = GetParametersForMethod(methodInfo, parsedArguments);

        parsedArguments.unRecognizedArguments = parsedArguments.unRecognizedArguments;
//                .Where(unrecognized=>unrecognized.Index>=1); //NOTE: should be different!

        ParsedMethod p = new ParsedMethod(parsedArguments);
        p.recognizedAction = methodInfo;
        p.recognizedActionParameters = recognizedActionParameters;
        p.recognizedClass = type;

        return p;
    }

    private static Method FindMethod(List<Method> methods, String methodName, List<Token> lexer) {
        List<Method> potential = new LinkedList<Method>();
        for (int i = 0; i < methods.size(); i++) {
            Method m = methods.get(i);
            if (m.getName().equalsIgnoreCase(methodName)) {
                potential.add(m);
            }
        }
        int count = 0;
        for (int i = 0; i < lexer.size(); i++) {
            if (lexer.get(i).TokenType == TokenType.Parameter) {
                count += 1;
            }
        }

        List<Method> potential_1 = new LinkedList<Method>();

        for (int i = 0; i < potential.size(); i++) {
            Method m = potential.get(i);
            if (m.getParameterTypes().length <= count) {
                potential_1.add(m);
            }
        }
        Collections.sort(potential_1, new Comparator<Method>() {

            public int compare(Method o1, Method o2) {
                return new Integer(o1.getParameterTypes().length).compareTo(o2.getParameterTypes().length);
            }
        });

        if (potential_1.size() > 0) {
            return potential_1.get(0);
        }
        if (potential.size() > 0) {
            return potential.get(0);
        }
        return null;
    }

    public String ClassName() {
        return type.getSimpleName().replace("Controller", "");
    }

    public List<Method> GetMethods() {
        MethodInfoFinder m = new MethodInfoFinder();
        return m.WithoutName(m.GetOwnPublicMethods(type), "help");

    }

    private Method FindMethodInfo(List<Token> arg) {
        if (arg.size() <= 2) {
            return null;
        }
        boolean foundClassName = ClassName().equalsIgnoreCase(arg.get(0).Value);
        if (foundClassName) {
            String methodName = arg.get(1).Value;
            Method methodInfo = FindMethod(GetMethods(), methodName, arg);
            return methodInfo;
        }
        return null;
    }

    private Object ConvertFrom(RecognizedArgument arg1, Class parameterInfo) {
        try {

            return _typeConverter.convert(parameterInfo, arg1.value);
        } catch (Exception e) {
            throw new TypeConversionFailedException()/*
                     * ("Could not convert argument", e){
                     * Argument=arg1.WithOptions.Argument.ToString(),
                     * Value=arg1.Value, TargetType=parameterInfo.ParameterType 
                }
                     */;
        }
    }
}
