package isop.command_line.parse

import isop.domain.Argument
import isop.infrastructure.KeyValuePair
import isop.infrastructure.Objects

/**
 *
 * @author mathieu
 */
class RecognizedArgument @JvmOverloads constructor(
        val argument: Argument,
        val index: Int,
        /// <summary>
        /// the "argument" of the expression "--argument"
        /// </summary>
        val rawArgument: String,
        /// <summary>
        /// the matched value if any, for instance the "value" of the expression "--argument value"
        /// </summary>

        val value: String? = null) {
    var inferredOrdinal: Boolean = false

    override fun hashCode(): Int {
        var hash = 7
        hash = 79 * hash + Objects.hashCode(this.value)
        hash = 79 * hash + Objects.hashCode(this.index)
        hash = 79 * hash + Objects.hashCode(this.rawArgument)
        hash = 79 * hash + Objects.hashCode(this.argument)
        return hash
    }

    override fun equals(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        if (javaClass != obj.javaClass) {
            return false
        }
        val other = obj as RecognizedArgument?
        return Objects.equals(this.value, other!!.value) &&
                Objects.equals(this.index, other.index) &&
                Objects.equals(this.rawArgument, other.rawArgument) &&
                Objects.equals(this.argument, other.argument)
    }

    override fun toString(): String {
        return String.format("%1s: %2s", this.argument, this.value)
    }

    fun asKeyValue(): KeyValuePair<String, String?> {
        return KeyValuePair(rawArgument, value)
    }
}
