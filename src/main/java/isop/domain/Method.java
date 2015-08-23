package isop.domain;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by mathieu.
 */
public class Method {
    private final java.lang.reflect.Method _method;
    public final String name;
    public Controller controller;

    public Method(java.lang.reflect.Method method) {
        _method = method;
        name = method.getName();
    }

    public Object invoke(Object instance, Object[] objects) throws InvocationTargetException, IllegalAccessException {
        return _method.invoke(instance, objects);
    }



    public List<Parameter> getParameters(){
        Class<?>[] types= _method.getParameterTypes();
        return Arrays.asList(types)
                .stream()
                .map(Parameter::new)
                .collect(Collectors.toList());
    }
    public List<Argument> getArguments() {
        List<Parameter> parameterInfos = getParameters();
        List<Argument> recognizers = new ArrayList<>();

        for (Parameter paramInfo : parameterInfos) {
            if (paramInfo.isClass()) {
                addArgumentWithOptionsForPropertiesOnObject(recognizers, paramInfo);
                //var obj = Activator.createInstance(paramInfo.ParameterType);

            } else {
                throw new RuntimeException("");
                //var recognizedArgument =  parsedArguments.RecognizedArguments.First(
                //    a => a.argument.ToUpperInvariant().Equals(paramInfo.name.ToUpperInvariant()));
                //parameters.Add( ConvertFrom (recognizedArgument, paramInfo.ParameterType));
            }
        }
        return recognizers;
    }

    private void addArgumentWithOptionsForPropertiesOnObject(List<Argument> recognizers, Parameter paramInfo) {
        String noDescription=null;
        Consumer<String> noAction=null;
        recognizers.addAll(paramInfo.getPublicInstanceProperties()
                .stream()
                .map(field->new Argument(
                        field.name,
                        noAction,
                        field.looksRequired,
                        noDescription,
                        field.propertyType))
                .collect(Collectors.toList()));

    }
}
