package isop.command_line

import isop.command_line.lex.Token
import isop.command_line.lex.TokenType
import isop.domain.Conventions

/**
 * Created by mathieu.
 */
object RewriteLexedTokensToSupportHelpAndIndex {
    fun rewrite(tokens: MutableList<Token>): List<Token> {
        //"--command"
        if (tokens.size >= 2
                && tokens[0].tokenType == TokenType.ARGUMENT
                && Conventions.equalsHelp(tokens[0].value)
                && tokens[1].tokenType == TokenType.ARGUMENT) {
            tokens[1] = Token(tokens[1].value, TokenType.PARAMETER_VALUE, tokens[1].index)
            tokens.add(1, Token("command", TokenType.PARAMETER, 1))
            //index:2
            if (tokens.size >= 4) {
                tokens[3] = Token(tokens[3].value, TokenType.PARAMETER_VALUE, tokens[1].index)
            }
            tokens.add(3, Token("action", TokenType.PARAMETER, 2))
        }
        //help maps to index (should have routing here)
        if (tokens.size == 0) {
            tokens.add(Token(Conventions.Help, TokenType.ARGUMENT, 0))
        }

        //Index rewrite:
        val indexToken = Token(Conventions.Index, TokenType.ARGUMENT, 1)
        if (tokens.size >= 2
                && tokens[1].tokenType != TokenType.ARGUMENT
                && tokens[0].tokenType == TokenType.ARGUMENT) {
            tokens.add(1, indexToken)
        } else if (tokens.size == 1 && tokens[0].tokenType == TokenType.ARGUMENT) {
            tokens.add(indexToken)
        }
        return tokens
    }
}
