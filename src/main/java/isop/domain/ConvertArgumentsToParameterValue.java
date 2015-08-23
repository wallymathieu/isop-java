package isop.domain;

import isop.MissingArgumentException;
import isop.TypeConversionFailedException;
import isop.infrastructure.Activator;
import isop.infrastructure.KeyValuePair;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by mathieu.
 */
public class ConvertArgumentsToParameterValue {

    private final BiFunction<Class, String, Object> typeConverter;

    public ConvertArgumentsToParameterValue(BiFunction<Class, String, Object> typeConverter) {
        if (typeConverter ==null){
            throw new NullPointerException("Missing type converter!");
        }
        this.typeConverter = typeConverter;
    }

    public Collection<Object> getParametersForMethod(Method method,
                                                     Collection<KeyValuePair<String, String>> parsedArguments,
                                                     Function<Class, Object> getInstance)
    {
        Collection<Parameter> parameterInfos = method.getParameters();
        List<Object> parameters = new ArrayList<>();

        for (Parameter paramInfo : parameterInfos)
        {
            if (paramInfo.isClass())
            {
                parameters.add(createObjectFromArguments(parsedArguments, paramInfo, getInstance));
            }
            else
            {
                throw new NotImplementedException();
            }
        }
        return parameters;
    }

    private Object createObjectFromArguments(Collection<KeyValuePair<String, String>> parsedArguments, Parameter paramInfo, Function<Class, Object> getInstance)
    {
        Object obj = Activator.createInstance(paramInfo.parameterClass, getInstance);
        for (
                FieldInfo prop : paramInfo.getPublicInstanceProperties())
        {
            KeyValuePair<String,String> recognizedArgument = parsedArguments.stream()
                    .filter(a -> a.key.equalsIgnoreCase(prop.name))
                    .findFirst()
                    .orElseThrow(() -> new MissingArgumentException("Missing argument", new String[]{prop.name}));
            try {
                prop.setValue(obj, convertFrom(recognizedArgument, prop.propertyType));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return obj;
    }

    private Object convertFrom(KeyValuePair<String, String> arg1, Class type)
    {
        try
        {
            return typeConverter.apply(type, arg1.value);
        }
        catch (Exception e)
        {
            throw new TypeConversionFailedException("Could not convert value", e, arg1, type);
        }
    }

}
