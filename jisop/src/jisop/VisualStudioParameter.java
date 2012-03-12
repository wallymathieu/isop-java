package jisop;

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
                Pattern.compile("(?<prefix>\\&?)(?<alias>.)[^=:]*(?<equals>[=:]?)");

        public static ArgumentParameter TryParse(String value)
        {
            //TODO: need to do some cleaning here
            Matcher match = VisualStudioArgPattern.matcher(value);
            if (match.matches())
            {
                LinkedList<String> aliases = new LinkedList<String>();
                String val;
                if (match.group("prefix").length() > 0)
                {
                    val = value.replace("&", "");
                    if (match.group("alias").length() > 0)
                        aliases.add(match.group("alias"));
                }
                else
                {
                    val = value;
                }
                String delimiter;
                if (match.group("equals").length() > 0)
                {
                    delimiter = match.group("equals");
                    val = val.replace(delimiter, "");
                }
                else delimiter = null;
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
