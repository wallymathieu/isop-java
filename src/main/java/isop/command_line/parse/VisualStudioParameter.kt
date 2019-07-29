package isop.command_line.parse

import java.util.LinkedList
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 *
 * @author mathieu
 */
internal object VisualStudioParameter {
    /// <summary>
    /// same pattern as in visual studio external tools: &amp;tool
    /// </summary>

    val VisualStudioArgPattern = Pattern.compile("(&?)(.)[^=:]*([=:]?)")

    fun tryParse(value: String): ArgumentParameter? {
        //TODO: need to do some cleaning here
        val match = VisualStudioArgPattern.matcher(value)
        if (match.matches()) {
            val aliases = LinkedList<String>()
            var `val`: String
            if (match.group(1).length > 0) {
                `val` = value.replace("&", "")
                if (match.group(2).length > 0) {
                    aliases.add(match.group(2))
                }
            } else {
                `val` = value
            }
            val delimiter: String?
            if (match.group(3).length > 0) {
                delimiter = match.group(3)
                `val` = `val`.replace(delimiter!!, "")
            } else {
                delimiter = null
            }
            aliases.add(`val`)

            return ArgumentParameter(
                    value,
                    aliases.toTypedArray(),
                    delimiter, null)

        }
        return null
    }
}
