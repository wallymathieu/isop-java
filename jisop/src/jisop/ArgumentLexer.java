package jisop;

import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mathieu
 */
public class ArgumentLexer extends LinkedList<Token> {

    private static final Pattern ParamPattern =
            Pattern.compile("(--|/|-)([^:=]*)([:=]?)(.*)");

    private ArgumentLexer() {
    }

    public ArgumentLexer(Collection<Token> tokens) {
        super(tokens);
    }

    public static ArgumentLexer Lex(String[] arg) {
        ArgumentLexer retval = new ArgumentLexer();
        int currentIndex = 0;
        int length = arg.length;
        while (currentIndex < length) {
            String value = arg[currentIndex];
            int valueIndex = currentIndex;
            currentIndex++;

            Matcher match = ParamPattern.matcher(value);
            if (match.matches()) {
                retval.add(new Token(match.group(2), TokenType.Parameter, valueIndex));
                if (match.group(4).length() > 0) {
                    retval.add(new Token(match.group(4), TokenType.ParameterValue, valueIndex));
                } else {
                    if (currentIndex < length) {
                        String possibleParamValue = arg[currentIndex];
                        int possibleParamValueIndex = currentIndex;
                        Matcher m = ParamPattern.matcher(possibleParamValue);
                        if (!m.matches()) {
                            currentIndex++;
                            retval.add(new Token(possibleParamValue, TokenType.ParameterValue, possibleParamValueIndex));
                        }
                    }
                }
            } else {
                retval.add(new Token(value, TokenType.Argument, valueIndex));
            }
        }
        return retval;
    }
}
