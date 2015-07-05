package jisop.command_line.parse;

import jisop.command_line.parse.ArgumentParameter;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mathieu
 */
class VisualStudioParameter {
    /// <summary>
    /// same pattern as in visual studio external tools: &amp;tool
    /// </summary>

    public static final Pattern VisualStudioArgPattern =
            Pattern.compile("(\\&?)(.)[^=:]*([=:]?)");

    public static ArgumentParameter tryParse(String value) {
        //TODO: need to do some cleaning here
        Matcher match = VisualStudioArgPattern.matcher(value);
        if (match.matches()) {
            LinkedList<String> aliases = new LinkedList<String>();
            String val;
            if (match.group(1).length() > 0) {
                val = value.replace("&", "");
                if (match.group(2).length() > 0) {
                    aliases.add(match.group(2));
                }
            } else {
                val = value;
            }
            String delimiter;
            if (match.group(3).length() > 0) {
                delimiter = match.group(3);
                val = val.replace(delimiter, "");
            } else {
                delimiter = null;
            }
            aliases.add(val);

            return new ArgumentParameter(
                    value,
                    aliases.toArray(new String[0]),
                    delimiter,
                    null);

        }
        return null;
    }
}
