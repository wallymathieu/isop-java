package isop.domain

import java.util.function.BiFunction

/**
 * Created by mathieu.
 */
class DefaultTypeConverter : BiFunction<Class<*>, String, Any> {
    override fun apply(cls: Class<*>, value: String): Any {
        if (cls == String::class.java) {
            return value
        }
        if (cls == Int::class.java) {
            return Integer.parseInt(value)
        }
        if (cls == Int::class.javaPrimitiveType) {
            return Integer.parseInt(value)
        }
        if (cls == Double::class.javaPrimitiveType) {
            return java.lang.Double.parseDouble(value)
        }
        throw RuntimeException("not implemented " + cls.name)
    }
}