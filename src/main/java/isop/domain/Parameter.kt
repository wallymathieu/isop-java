package isop.domain

import java.lang.reflect.Field
import java.util.Arrays
import java.util.regex.Pattern
import java.util.stream.Collectors

/**
 * Created by mathieu.
 */
class Parameter(val parameterClass: Class<*>) {

    val isClass: Boolean
        get() = !parameterClass.isPrimitive

    // TODO: Add getters and setters here as well!
    val publicInstanceProperties: Collection<FieldInfo>
        get() = getFields(parameterClass)

    private inner class FieldInfoFromField(private val f: Field) : FieldInfo() {

        init {
            this.name = f.name
            this.propertyType = f.type
            this.looksRequired = Arrays.asList(*f.declaredAnnotations)
                    .any { a ->
                        notNull.matcher(a.annotationClass.simpleName).find()
                    }
        }

        @Throws(IllegalArgumentException::class, IllegalAccessException::class)
        override fun setValue(obj: Any, value: Any) {
            f.set(obj, value)
        }
    }

    private fun getFields(p: Class<*>): List<FieldInfo> {
        val fields = Arrays.asList(*p.fields)
        return fields
                .map( { FieldInfoFromField(it) })
                .toList<FieldInfo>()
    }

    companion object {
        private val notNull = Pattern.compile("(not|non)null", Pattern.CASE_INSENSITIVE)
    }
}
