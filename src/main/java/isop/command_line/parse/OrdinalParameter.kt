package isop.command_line.parse

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 *
 * @author mathieu
 */
object OrdinalParameter {
    private val pattern = Pattern.compile("#(\\d*)(.*)")
    fun tryParse(value: String): ArgumentParameter? {
        val match = pattern.matcher(value)
        if (match.matches()) {
            val rest = match.group(2)
            val param = ArgumentParameter.parse(rest)
            return ArgumentParameter(value,
                    param.aliases, param.delimiter,
                    Integer.parseInt(match.group(1)))
        }
        return null
    }
}
