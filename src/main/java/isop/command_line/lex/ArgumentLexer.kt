package isop.command_line.lex

import java.util.LinkedList
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 *
 * @author mathieu
 */
class ArgumentLexer private constructor() : LinkedList<Token>() {
    companion object {

        private val PARAM_PATTERN = Pattern.compile("(--|/|-)([^:=]*)([:=]?)(.*)")

        fun lex(arg: Array<String>): ArgumentLexer {
            val collector = ArgumentLexer()
            var currentIndex = 0
            val length = arg.size
            while (currentIndex < length) {
                val value = arg[currentIndex]
                val valueIndex = currentIndex
                currentIndex++

                val match = PARAM_PATTERN.matcher(value)
                if (match.matches()) {
                    collector.add(Token(match.group(2), TokenType.PARAMETER, valueIndex))
                    if (match.group(4).length > 0) {
                        collector.add(Token(match.group(4), TokenType.PARAMETER_VALUE, valueIndex))
                    } else {
                        if (currentIndex < length) {
                            val possibleParamValue = arg[currentIndex]
                            val possibleParamValueIndex = currentIndex
                            val m = PARAM_PATTERN.matcher(possibleParamValue)
                            if (!m.matches()) {
                                currentIndex++
                                collector.add(Token(possibleParamValue, TokenType.PARAMETER_VALUE, possibleParamValueIndex))
                            }
                        }
                    }
                } else {
                    collector.add(Token(value, TokenType.ARGUMENT, valueIndex))
                }
            }
            return collector
        }
    }
}
