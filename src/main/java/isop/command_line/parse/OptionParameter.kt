package isop.command_line.parse

import isop.infrastructure.Strings

/**
 *
 * @author mathieu
 */
internal object OptionParameter {
    fun tryParse(value: String): ArgumentParameter? {
        if (value.contains("|")) {
            val names = Strings.split(Strings.trimEnd(value, "=:"), '|')
            var delimiter: String? = null
            val last = value.substring(value.length - 2, value.length - 1)
            when (last[0]) {
                '=', ':' -> delimiter = last
                else -> {
                }
            }
            return ArgumentParameter(value, names, delimiter, null)
        }
        return null
    }
}
