package jisop.command_line.parse;

import jisop.infrastructure.Strings;

/**
 *
 * @author mathieu
 */
class OptionParameter {
       public static ArgumentParameter tryParse(String value)
        {
            if (value.contains("|"))
            {
                String[] names = Strings.split(Strings.trimEnd(value, "=:"), '|');
                String delimiter = null;
                String last = value.substring(value.length()-2, value.length()-1);
                switch (last.charAt(0))
                {
                    case '=':
                    case ':':
                        delimiter = last;
                        break;
                    default:
                        break;
                }
                return new ArgumentParameter(value, names, delimiter, null);
            }
            return null;
        }
}
