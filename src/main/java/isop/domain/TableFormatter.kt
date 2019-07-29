package isop.domain

import java.lang.reflect.Field
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import java.util.stream.StreamSupport

/**
 * Created by mathieu.
 */
class TableFormatter : Formatter {

    override fun format(value: Any): Stream<String> {
        if (value != null) {
            if (value is String) {
                return Stream.of(value)
            }
            if (value.javaClass.isPrimitive) {
                return Stream.of(value.toString())
            }
            if (value.javaClass.isArray) {
                val array = Arrays.asList(*value as Array<Any>)
                return getStringStream(array.stream())
            }
            return if (value is Collection<*>) {
                getStringStream(value.stream())
            } else (value as? Stream<*>)?.let { getStringStream(it) } ?: getStringStream(Stream.of(value))
        }
        return Stream.empty()
    }

    private fun getStringStream(value: Stream<out Any?>): Stream<String> {
        val it = value.iterator()
        val fields: Array<Field>
        if (it.hasNext()) {
            val head = it.next() ?: throw NullPointerException("head")
            val type = head.javaClass
            fields = getFields(type)

            return Stream.concat(Stream.of(
                    header(fields),
                    line(fields, head)),
                    StreamSupport.stream(Spliterators.spliteratorUnknownSize(it,Spliterator.ORDERED), false)
                    .map { o -> line(fields, o!!) })
        }
        return Stream.empty()
    }


    private fun getFields(t: Class<*>): Array<Field> {
        return t.fields
        //return t.GetProperties(BindingFlags.Public | BindingFlags.Instance | BindingFlags.GetProperty);
    }

    private fun header(fields: Array<Field>): String {
        return listOf(*fields)
                .map { it.name }
                .toList<String>().joinToString("\t")
    }

    private fun line(fields: Array<Field>, item: Any): String {
        return listOf(*fields)
                .stream()
                .map { prop -> getValue(prop, item) }
                .collect(Collectors.toList<String>()).joinToString("\t")
    }

    private operator fun getValue(field: Field, item: Any): String {
        try {
            return field.get(item).toString()
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }

    }
}
