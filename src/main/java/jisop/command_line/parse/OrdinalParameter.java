package jisop.command_line.parse;

import jisop.command_line.parse.ArgumentParameter;

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
                String prototype = value;
                String rest = match.group(2);
                ArgumentParameter param = ArgumentParameter.parse(rest);
                return new ArgumentParameter(prototype, 
                        param.Aliases, param.Delimiter, 
                        Integer.parseInt(match.group(1)));
            }
            return null;
        }
}
