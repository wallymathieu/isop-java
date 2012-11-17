package jisop;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
class ControllerRecognizer {

    private Class type;
    boolean ignoreGlobalUnMatchedParameters = false;
    Transform _transform = new Transform();
    private TypeConverter _typeConverter;
    private ObjectFactory _factory;

    private Collection<UnrecognizedArgument> withoutIndex0to1(Collection<UnrecognizedArgument> unRecognizedArguments) {
        List<UnrecognizedArgument> retval = new ArrayList<UnrecognizedArgument>();
        for(UnrecognizedArgument arg : unRecognizedArguments){
            if (arg.index>=2)
                retval.add(arg);
        }
        return retval;
    }
    private class DefaultTypeConverter implements TypeConverter
    {
        @Override
        public Object convert(Class cls, String value) {
            if (cls==String.class){
                return value;
            }
            if (cls==Integer.class){
                return Integer.parseInt(value);
            }
            if (cls==int.class){
               return Integer.parseInt(value);
            }
            if (cls==double.class){
                return Double.parseDouble(value);
            }
            throw new RuntimeException("not implemented "+cls.getName());
        }
    }

    ControllerRecognizer(Class arg, ObjectFactory factory) {
        type = arg;
        _typeConverter=new DefaultTypeConverter();
        if (null==factory){
            throw new RuntimeException("factory missing");
        }
        _factory=factory;
    }

    boolean recognize(String[] arg) {
        ArgumentLexer lexer = _transform.rewrite(ArgumentLexer.lex(arg));
        return null != findMethodInfo(lexer);
    }

    private abstract class PropertyInfo {

        public String name;
        public Class propertyType;

        public abstract void setValue(Object obj, Object value) throws IllegalArgumentException, IllegalAccessException;
    }

    private class PropertyInfoFromField extends PropertyInfo {

        private Field f;

        public PropertyInfoFromField(Field f) {
            this.f = f;
            this.name = f.getName();
            this.propertyType = f.getType();
        }

        @Override
        public void setValue(Object obj, Object value) throws IllegalArgumentException, IllegalAccessException {
            f.set(obj, value);
        }
    }

    private PropertyInfo[] getProperties(Class<?> p) {
        Field[] fields = p.getFields();
        PropertyInfo[] props = new PropertyInfo[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            props[i] = new PropertyInfoFromField(f);
        }
        return props;
    }

    private boolean IsClass(Class<?> p) {
        return true;
    }

    private List<ArgumentWithOptions> getRecognizers(Method method) {
        Class<?>[] parameterInfos = method.getParameterTypes();

        List<ArgumentWithOptions> recognizers = new ArrayList<ArgumentWithOptions>();

        for (Class<?> paramInfo : parameterInfos) {
            if (IsClass(paramInfo)) {
                //var obj = Activator.CreateInstance(paramInfo.ParameterType);
                for (PropertyInfo prop : getProperties(paramInfo)) {
                    ArgumentWithOptions arg = new ArgumentWithOptions(
                            ArgumentParameter.parse(prop.name), true, "", null);
                    recognizers.add(arg);
                }
            } else {
                throw new RuntimeException("");
                //var recognizedArgument =  parsedArguments.RecognizedArguments.First(
                //    a => a.Argument.ToUpperInvariant().Equals(paramInfo.Name.ToUpperInvariant()));
                //parameters.Add( ConvertFrom (recognizedArgument, paramInfo.ParameterType));
            }
        }        
        return recognizers;
    }

    ParsedMethod parse(String[] arg) {
        ArgumentLexer lexer = _transform.rewrite(ArgumentLexer.lex(arg));

        Method methodInfo = findMethodInfo(lexer);

        List<ArgumentWithOptions> argumentRecognizers = getRecognizers(methodInfo);

        ArgumentParser parser = new ArgumentParser(argumentRecognizers);
        ParsedArguments parsedArguments = parser.parse(lexer, arg);
        Object instance = _factory.build(type);
        return parse(instance, methodInfo, parsedArguments);
    }

    private Object newInstance(Class<?> type, Object instance) {
        try {
            
            for(Constructor c: type.getConstructors()){
                if (c.getParameterTypes().length==0){
                    return c.newInstance(new Object[0]);
                }
            }
            for (Constructor c: type.getConstructors()) {
                if (c.getParameterTypes().length==1 
                        && c.getParameterTypes()[0].isAssignableFrom(instance.getClass())){
                    Object[] args = new Object[1];
                    args[0]=instance;
                    return c.newInstance(args);
                }
            }
            throw new RuntimeException("Could not find constructor");
            //return type.newInstance();
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ControllerRecognizer.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Class: " + type.getName(), ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ControllerRecognizer.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Class: " + type.getName(), ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ControllerRecognizer.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Class: " + type.getName(), ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ControllerRecognizer.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Class: " + type.getName(), ex);
        }

    }

    private List<Object> getParametersForMethod(Object instance, Method method, ParsedArguments parsedArguments) {
        Class<?>[] parameterInfos = method.getParameterTypes();

        List<Object> parameters = new ArrayList<Object>();

        for (Class<?> paramInfo : parameterInfos) {
            if (IsClass(paramInfo)) {
                try {
                    Object obj = newInstance(paramInfo,instance);
                    for (PropertyInfo prop : getProperties(paramInfo)) {
                        RecognizedArgument recognizedArgument = parsedArguments.withName(prop.name);


                        prop.setValue(obj, convertFrom(recognizedArgument, prop.propertyType));

                    }
                    parameters.add(obj);
                } catch (IllegalAccessException ex2) {
                    throw new RuntimeException(ex2);
                } catch (IllegalArgumentException ex3) {
                    System.err.println(ex3.getMessage());
                    throw new RuntimeException("Class: " + paramInfo.getName(), ex3);
                } 
            } else {
                throw new RuntimeException();
                //var recognizedArgument =  parsedArguments.RecognizedArguments.First(
                //    a => a.Argument.ToUpperInvariant().Equals(paramInfo.Name.ToUpperInvariant()));
                //parameters.Add( ConvertFrom (recognizedArgument, paramInfo.ParameterType));
            }
        }

        return parameters;
    }

    public ParsedMethod parse(Object instance, Method methodInfo, ParsedArguments parsedArguments) {
        Collection<ArgumentWithOptions> unMatchedRequiredArguments = parsedArguments.UnMatchedRequiredArguments();
        if (unMatchedRequiredArguments.size() > 0) {
            throw new MissingArgumentException();
//                          {
//                              Arguments = unMatchedRequiredArguments
//                                  .Select(unmatched => new KeyValuePair<string, string>(unmatched.Argument.ToString(), unmatched.Argument.Help())).ToList()
//                          };
        }

        List<Object> recognizedActionParameters = getParametersForMethod(instance, methodInfo, parsedArguments);

        parsedArguments.unRecognizedArguments = withoutIndex0to1( parsedArguments.unRecognizedArguments);
//                .Where(unrecognized=>unrecognized.Index>=1); //NOTE: should be different!

        ParsedMethod p = new ParsedMethod(parsedArguments);
        p.recognizedAction = methodInfo;
        p.recognizedActionParameters = recognizedActionParameters;
        p.recognizedInstance = instance;

        return p;
    }

    private static Method findMethod(List<Method> methods, String methodName, List<Token> lexer) {
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

    public String className() {
        return type.getSimpleName().replace("Controller", "");
    }

    public List<Method> getMethods() {
        MethodInfoFinder m = new MethodInfoFinder();
        return m.WithoutName(m.GetOwnPublicMethods(type), "help");

    }

    private Method findMethodInfo(List<Token> arg) {
        if (arg.size() <= 2) {
            return null;
        }
        boolean foundClassName = className().equalsIgnoreCase(arg.get(0).Value);
        if (foundClassName) {
            String methodName = arg.get(1).Value;
            Method methodInfo = findMethod(getMethods(), methodName, arg);
            return methodInfo;
        }
        return null;
    }

    private Object convertFrom(RecognizedArgument arg1, Class parameterInfo) {
        try {
            return _typeConverter.convert(parameterInfo, arg1.value);
        } catch (Exception e) {
            throw new TypeConversionFailedException("Parameter: "+parameterInfo.getName()+", value: "+arg1.value, e);
        }
    }
}
