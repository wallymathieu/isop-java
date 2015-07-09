package jisop.command_line.parse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mathieu
 */
public class OrdinalParameter {
       private static final Pattern pattern = 
               Pattern.compile("#(\\d*)(.*)");
       public static ArgumentParameter tryParse(String value)
        {
            Matcher match = pattern.matcher(value);
            if (match.matches())
            {
                String rest = match.group(2);
                ArgumentParameter param = ArgumentParameter.parse(rest);
                return new ArgumentParameter(value,
                        param.aliases, param.delimiter,
                        Integer.parseInt(match.group(1)));
            }
            return null;
        }
}
