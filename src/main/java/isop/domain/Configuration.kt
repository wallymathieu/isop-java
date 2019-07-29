package isop.domain

import java.util.ArrayList
import java.util.function.BiFunction
import java.util.function.Function

/**
 * Created by mathieu.
 */
class Configuration {
    val recognizes: MutableCollection<Controller>
    val fields: MutableCollection<Field>
    var factory: Function<Class<*>, Any>? = null
    var typeConverter: BiFunction<Class<*>, String, Any>
    var recognizesHelp: Boolean = false
    var formatter: Formatter

    init {
        recognizes = ArrayList()
        fields = ArrayList()
        formatter = ToStringFormatter()
        typeConverter = DefaultTypeConverter()
    }
}
