package isop.command_line.parse

import isop.infrastructure.Strings

import java.util.Arrays

/**
 *
 * @author mathieu
 */
class ArgumentParameter(val prototype: String, val aliases: Array<String>, val delimiter: String?, val ordinal: Int?) {

    fun help(): String {
        return ("--" + Strings.join(", or ", aliases)
                + if (Strings.isNullOrEmpty(delimiter))
            ""
        else
            " $delimiter")
    }

    override fun toString(): String {
        return prototype
    }

    fun hasAlias(value: String): Boolean {
        return Strings.containsStringIgnoreCase(aliases, value)
    }

    fun accept(index: Int, `val`: String): Boolean {
        return if (ordinal == null) {
            hasAlias(`val`)
        } else {
            ordinal == index && hasAlias(`val`)
        }
    }

    fun accept(`val`: String): Boolean {
        return hasAlias(`val`)
    }

    fun longAlias(): String {
        return Arrays.asList(*aliases)
                .stream()
                .max { a, b -> Integer.compare(a.length, b.length) }
                .orElse(null)
    }

    companion object {

        fun parse(value: String): ArgumentParameter {
            val ordinalParameter = OrdinalParameter.tryParse(value)
            if (ordinalParameter != null)
                return ordinalParameter
            val optionParameter = OptionParameter.tryParse(value)
            if (optionParameter != null)
                return optionParameter
            val visualStudioParameter = VisualStudioParameter.tryParse(value)
            if (visualStudioParameter != null)
                return visualStudioParameter
            throw RuntimeException("failed to parse $value")
        }
    }
}
