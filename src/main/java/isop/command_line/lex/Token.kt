package isop.command_line.lex


/**
 *
 * @author mathieu
 */
class Token(var value: String, var tokenType: TokenType?, /// <summary>
        /// the index in the argument array
        /// </summary>
            var index: Int) {

    override fun equals(obj: Any?): Boolean {
        return null != obj &&
                obj is Token &&
                this.equals(obj as Token?)
    }

    override fun hashCode(): Int {
        var hash = 3
        hash = 79 * hash + this.value.hashCode()
        hash = 79 * hash + if (this.tokenType != null) this.tokenType!!.hashCode() else 0
        return hash
    }

    fun equals(obj: Token?): Boolean {
        return null != obj &&
                value == obj.value &&
                tokenType == obj.tokenType
    }

    override fun toString(): String {
        return String.format("%1s: '%2s'", tokenType, value)
    }
}
