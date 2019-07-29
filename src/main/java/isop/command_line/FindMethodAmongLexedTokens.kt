package isop.command_line

import isop.command_line.lex.Token
import isop.command_line.lex.TokenType
import isop.domain.Method
import java.util.stream.Stream

/**
 * Created by mathieu.
 */
object FindMethodAmongLexedTokens {
    private fun getPotential(methods: Collection<Method>, methodName: String): Stream<Method> {
        return methods
                .stream()
                .filter { method -> method.name.equals(methodName, ignoreCase = true) }
    }

    fun findMethod(methods: Collection<Method>, methodName: String, lexed: Collection<Token>): Method {
        val count = lexed.stream().filter { t -> t.tokenType == TokenType.PARAMETER }.count()
        val potentialMethod = getPotential(methods, methodName)
                .filter { method -> method.parameters.size <= count }
                .sorted { o1, o2 ->
                    Integer.compare(
                            o1.parameters.size,
                            o2.parameters.size)
                }
                .findAny()
                .orElse(null)
        return potentialMethod ?: getPotential(methods, methodName)
                .findAny()
                .orElse(null)
    }
}
