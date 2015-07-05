/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop.command_line.lex;

import jisop.command_line.lex.ArgumentLexer;
import jisop.command_line.lex.Token;
import jisop.command_line.lex.TokenType;

/**
 *
 * @author mathieu
 */
public class Transform {
 
        // Lexer -> 
        // Arg(ControllerName),Param(..),.. -> Arg(ControllerName),Arg('Index'),... 
        public ArgumentLexer rewrite(ArgumentLexer tokens)
        {
            //"--command"
            if (tokens.size() >= 2 
                && tokens.get(0).TokenType== TokenType.Argument
                && tokens.get(0).Value.equalsIgnoreCase("help")
                && tokens.get(1).TokenType==TokenType.Argument)
            {
                tokens.set(1, new Token(tokens.get(1).Value,TokenType.ParameterValue,tokens.get(1).Index));
                tokens.add(1, new Token("command",TokenType.Parameter,1));
                //index:2
                if (tokens.size() >= 4) { 
                    tokens.set(3,  new Token(tokens.get(3).Value, TokenType.ParameterValue, tokens.get(1).Index));
                }
                tokens.add(3, new Token("action", TokenType.Parameter, 2));
            }
            //help maps to index (should have routing here)
            if (tokens.size() == 0)
            {
                tokens.add(new Token("help",TokenType.Argument,0));
            }

            //Index rewrite:
            Token indexToken= new Token("Index", TokenType.Argument,1);
            if (tokens.size()>=2 
                && tokens.get(1).TokenType!=TokenType.Argument 
                && tokens.get(0).TokenType==TokenType.Argument)
            {
                tokens.add(1,indexToken);
            }
            else if (tokens.size()==1 
                && tokens.get(0).TokenType==TokenType.Argument)
            {
                tokens.add(indexToken);
            }
            return new ArgumentLexer(tokens);
        }
    
}
