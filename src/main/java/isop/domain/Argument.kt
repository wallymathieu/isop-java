package isop.domain

import java.util.Objects
import java.util.function.Consumer

/**
 * Created by mathieu.
 */
open class Argument(val name: String, val action: Consumer<String>?, val required: Boolean, val description: String?) {

    override fun hashCode(): Int {
        var hash = 7
        hash = 79 * hash + Objects.hashCode(this.description)
        hash = 79 * hash + Objects.hashCode(this.name)
        hash = 79 * hash + Objects.hashCode(this.required)
        return hash
    }

    override fun equals(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        if (javaClass != obj.javaClass) {
            return false
        }
        val other = obj as Argument?
        return this.description == other!!.description &&
                this.name == other.name &&
                this.required == other.required
    }

    override fun toString(): String {
        return String.format("%s:%s", name, description)
    }
}
