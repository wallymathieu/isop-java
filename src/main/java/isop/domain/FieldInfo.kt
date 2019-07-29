package isop.domain

/**
 * Created by mathieu.
 *
 * The reasoning behind this class is to be able to handle getters and setters in the same way as fields.
 */
abstract class FieldInfo {

    var name: String? = null
    var propertyType: Class<*>? = null
    var looksRequired: Boolean = false

    @Throws(IllegalArgumentException::class, IllegalAccessException::class)
    abstract fun setValue(obj: Any, value: Any)
}