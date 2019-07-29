package isop.domain

import isop.MissingArgumentException
import isop.TypeConversionFailedException
import isop.abstractions.NotImplementedException
import isop.infrastructure.Activator
import isop.infrastructure.KeyValuePair

import java.util.ArrayList
import java.util.function.BiFunction
import java.util.function.Function

/**
 * Created by mathieu.
 */
class ConvertArgumentsToParameterValue(private val typeConverter: BiFunction<Class<*>, String, Any>) {

    fun getParametersForMethod(method: Method,
                               parsedArguments: Collection<KeyValuePair<String, String?>>,
                               getInstance: Function<Class<*>, Any?>): Collection<Any> {
        val parameterInfos = method.parameters
        val parameters = ArrayList<Any>()

        for (paramInfo in parameterInfos) {
            if (paramInfo.isClass) {
                parameters.add(createObjectFromArguments(parsedArguments, paramInfo, getInstance))
            } else {
                throw NotImplementedException()
            }
        }
        return parameters
    }

    private fun createObjectFromArguments(parsedArguments: Collection<KeyValuePair<String, String?>>, paramInfo: Parameter, getInstance: Function<Class<*>, Any?>): Any {
        val obj = Activator.createInstance(paramInfo.parameterClass, getInstance)
        for (prop in paramInfo.publicInstanceProperties) {
            val recognizedArgument = parsedArguments
                    .find { a -> a.key.equals(prop.name, ignoreCase = true) }
                    ?: throw MissingArgumentException("Missing argument", arrayOf(prop.name))
            try {
                val value=convertFrom(recognizedArgument, prop.propertyType);
                if (value!=null)
                    prop.setValue(obj, value)
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            }

        }
        return obj
    }

    private fun convertFrom(arg1: KeyValuePair<String, String?>, type:Class<*>?): Any? {
        if (type==null)
            throw TypeConversionFailedException("Could not convert value", null, arg1, type)
        try {
            if (arg1.value==null)
                return null
            return typeConverter.apply(type, arg1.value)
        } catch (e: Exception) {
            throw TypeConversionFailedException("Could not convert value", e, arg1, type)
        }

    }

}
