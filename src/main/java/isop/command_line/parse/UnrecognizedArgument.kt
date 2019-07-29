package isop.command_line.parse

/**
 *
 * @author mathieu
 */
class UnrecognizedArgument(val index: Int, val value: String?) {
    override fun hashCode(): Int {
        var hash = 3
        hash = 41 * hash + this.index
        hash = 41 * hash + if (this.value != null) this.value.hashCode() else 0
        return hash
    }

    override fun equals(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        if (javaClass != obj.javaClass) {
            return false
        }
        val other = obj as UnrecognizedArgument?
        return this.index == other!!.index && this.value == other.value
    }

}
