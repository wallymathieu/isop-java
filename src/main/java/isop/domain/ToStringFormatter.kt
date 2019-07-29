package isop.domain

import java.util.Arrays
import java.util.stream.Stream

/**
 * Created by mathieu.
 */
class ToStringFormatter : Formatter {
    override fun format(value: Any): Stream<String> {
        if (value != null) {
            if (value is String) {
                return Stream.of(value)
            }
            if (value.javaClass.isPrimitive) {
                return Stream.of(value.toString())
            }
            if (value.javaClass.isArray) {
                return toStrings(listOf(*value as Array<Any>).stream())
            }
            return if (value is Collection<*>) {
                toStrings(value.stream())
            } else (value as? Stream<*>)?.let { toStrings(it) } ?: Stream.of(value.toString())
        }
        return Stream.empty()
    }

    private fun toStrings(value: Stream<*>): Stream<String> {
        return value.map { it.toString() }
    }
}
