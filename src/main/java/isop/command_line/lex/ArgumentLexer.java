package isop.command_line.lex;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mathieu
 */
public class ArgumentLexer extends LinkedList<Token> {

    private static final Pattern PARAM_PATTERN =
            Pattern.compile("(--|/|-)([^:=]*)([:=]?)(.*)");

    private ArgumentLexer() {
    }

    public static ArgumentLexer lex(String[] arg) {
        ArgumentLexer collector = new ArgumentLexer();
        int currentIndex = 0;
        int length = arg.length;
        while (currentIndex < length) {
            String value = arg[currentIndex];
            int valueIndex = currentIndex;
            currentIndex++;

            Matcher match = PARAM_PATTERN.matcher(value);
            if (match.matches()) {
                collector.add(new Token(match.group(2), TokenType.PARAMETER, valueIndex));
                if (match.group(4).length() > 0) {
                    collector.add(new Token(match.group(4), TokenType.PARAMETER_VALUE, valueIndex));
                } else {
                    if (currentIndex < length) {
                        String possibleParamValue = arg[currentIndex];
                        int possibleParamValueIndex = currentIndex;
                        Matcher m = PARAM_PATTERN.matcher(possibleParamValue);
                        if (!m.matches()) {
                            currentIndex++;
                            collector.add(new Token(possibleParamValue, TokenType.PARAMETER_VALUE, possibleParamValueIndex));
                        }
                    }
                }
            } else {
                collector.add(new Token(value, TokenType.ARGUMENT, valueIndex));
            }
        }
        return collector;
    }
}
