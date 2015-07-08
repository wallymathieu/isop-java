package jisop.command_line;

import jisop.command_line.lex.Token;
import jisop.command_line.lex.TokenType;
import jisop.domain.Method;

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
                .filter(method -> method.GetParameters().size() <= count)
                .sorted((o1, o2) -> Integer.compare(
                        o1.GetParameters().size(),
                        o2.GetParameters().size()))
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
