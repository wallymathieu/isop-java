package jisop;

/**
 *
 * @author mathieu
 */
class OptionParameter {
       public static  ArgumentParameter TryParse(String value)
        {
            if (value.contains("|"))
            {
                String prototype = value;
                String[] names = StringUtils.trimEnd( prototype, "=:").split("|");
                String delimiter = null;
                String last = prototype.substring(prototype.length()-2, prototype.length()-1);
                switch (last)
                {
                    case "=":
                    case ":":
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
