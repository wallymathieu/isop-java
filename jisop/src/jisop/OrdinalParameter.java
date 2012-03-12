package jisop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mathieu
 */
public class OrdinalParameter {
       private static final Pattern pattern = 
               Pattern.compile("#(?<ord>\\d*)(?<rest>.*)");
       public static ArgumentParameter TryParse(String value)
        {
            Matcher match = pattern.matcher(value);
            if (match.matches())
            {
                String prototype = value;
                String rest = match.group("rest");
                ArgumentParameter param = ArgumentParameter.Parse(rest);
                return new ArgumentParameter(prototype, 
                        param.Aliases, param.Delimiter, 
                        Integer.parseInt(match.group("ord")));
            }
            return null;
        }
}
