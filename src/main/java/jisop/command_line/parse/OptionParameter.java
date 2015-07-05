package jisop.command_line.parse;

import jisop.infrastructure.StringUtils;

/**
 *
 * @author mathieu
 */
class OptionParameter {
       public static ArgumentParameter tryParse(String value)
        {
            if (value.contains("|"))
            {
                String prototype = value;
                String[] names = StringUtils.split(StringUtils.trimEnd(prototype, "=:"), '|');
                String delimiter = null;
                String last = prototype.substring(prototype.length()-2, prototype.length()-1);
                switch (last.charAt(0))
                {
                    case '=':
                    case ':':
                        delimiter = last;
                        break;
                    default:
                        break;
                }
                return new ArgumentParameter(prototype, names, delimiter, null);
            }
            return null;
        }
}