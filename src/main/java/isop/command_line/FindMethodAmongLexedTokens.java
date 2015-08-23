package isop.command_line;

import isop.command_line.lex.Token;
import isop.command_line.lex.TokenType;
import isop.domain.Method;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by mathieu.
 */
public class FindMethodAmongLexedTokens {
    private static Stream<Method> getPotential(Collection<Method> methods, String methodName){
        return methods
                .stream()
                .filter(method -> method.name.equalsIgnoreCase(methodName))
                ;
    }

    public static Method findMethod(Collection<Method> methods, String methodName, Collection<Token> lexed)
    {
        long count = lexed.stream().filter(t -> t.tokenType.equals(TokenType.PARAMETER)).count();
        Method potentialMethod =
                getPotential(methods, methodName)
                .filter(method -> method.getParameters().size() <= count)
                .sorted((o1, o2) -> Integer.compare(
                        o1.getParameters().size(),
                        o2.getParameters().size()))
                .findAny()
                .orElse(null);
        if (potentialMethod != null)
        {
            return potentialMethod;
        }
        return getPotential(methods, methodName)
                .findAny()
                .orElse(null);
    }
}
