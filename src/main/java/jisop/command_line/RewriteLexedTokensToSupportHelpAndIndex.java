package jisop.command_line;

import jisop.command_line.lex.Token;
import jisop.command_line.lex.TokenType;
import jisop.domain.Conventions;

import java.util.List;

/**
 * Created by mathieu.
 */
public class RewriteLexedTokensToSupportHelpAndIndex {
    public static List<Token> rewrite(List<Token> tokens) {
        //"--command"
        if (tokens.size() >= 2
                && tokens.get(0).tokenType.equals(TokenType.ARGUMENT)
                && Conventions.equalsHelp(tokens.get(0).value)
                && tokens.get(1).tokenType.equals( TokenType.ARGUMENT))
        {
            tokens.set(1, new Token(tokens.get(1).value,TokenType.PARAMETER_VALUE,tokens.get(1).index));
            tokens.add(1,new Token("command",TokenType.PARAMETER,1));
            //index:2
            if (tokens.size() >= 4) {
                tokens.set(3,
                    new Token(tokens.get(3).value, TokenType.PARAMETER_VALUE, tokens.get(1).index));
            }
            tokens.add(3, new Token("action", TokenType.PARAMETER, 2));
        }
        //help maps to index (should have routing here)
        if (tokens.size() == 0)
        {
            tokens.add(new Token(Conventions.Help,TokenType.ARGUMENT,0));
        }

        //Index rewrite:
        Token indexToken= new Token(Conventions.Index, TokenType.ARGUMENT,1);
        if (tokens.size()>=2
                && tokens.get(1).tokenType !=TokenType.ARGUMENT
                && tokens.get(0).tokenType ==TokenType.ARGUMENT)
        {
            tokens.add(1,indexToken);
        }
        else if (tokens.size()==1
                && tokens.get(0).tokenType ==TokenType.ARGUMENT)
        {
            tokens.add(indexToken);
        }
        return tokens;
    }
}
