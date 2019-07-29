package isop.domain

import java.lang.reflect.InvocationTargetException
import java.util.ArrayList
import java.util.Arrays
import java.util.function.Consumer
import java.util.stream.Collectors

/**
 * Created by mathieu.
 */
class Method(private val _method: java.lang.reflect.Method) {
    val name: String = _method.name
    var controller: Controller? = null


    val parameters: List<Parameter>
        get() {
            val types = _method.parameterTypes
            return Arrays.asList(*types)
                    .map({p->Parameter(p)})
                    .toList()
        }
    //var obj = Activator.createInstance(paramInfo.ParameterType);
    //var recognizedArgument =  parsedArguments.RecognizedArguments.First(
    //    a => a.argument.ToUpperInvariant().Equals(paramInfo.name.ToUpperInvariant()));
    //parameters.Add( ConvertFrom (recognizedArgument, paramInfo.ParameterType));
    val arguments: List<Argument>
        get() {
            val parameterInfos = parameters
            val recognizers = ArrayList<Argument>()

            for (paramInfo in parameterInfos) {
                if (paramInfo.isClass) {
                    addArgumentWithOptionsForPropertiesOnObject(recognizers, paramInfo)

                } else {
                    throw RuntimeException("")
                }
            }
            return recognizers
        }

    @Throws(InvocationTargetException::class, IllegalAccessException::class)
    operator fun invoke(instance: Any, objects: Array<Any?>): Any? {
        return _method.invoke(instance, *objects)
    }

    private fun addArgumentWithOptionsForPropertiesOnObject(recognizers: MutableList<Argument>, paramInfo: Parameter) {
        val noDescription: String? = null
        val noAction: Consumer<String>? = null
        recognizers.addAll(paramInfo.publicInstanceProperties
                .map { field ->
                    Argument(
                            field.name!!,
                            noAction,
                            field.looksRequired,
                            noDescription)
                }
                .toList())

    }
}
