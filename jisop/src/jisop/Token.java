package jisop;

import java.util.Objects;

/**
 *
 * @author mathieu
 */
public class Token {

    public String Value;
    public TokenType TokenType;
    /// <summary>
    /// the index in the argument array
    /// </summary>
    public int Index;

    public Token(String value, TokenType tokenType, int index) {
        Value = value;
        TokenType = tokenType;
        Index = index;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (obj.getClass()!=Token.class){
            return false;
        }
        return this.equals((Token) obj);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.Value);
        hash = 79 * hash + (this.TokenType != null ? this.TokenType.hashCode() : 0);
        return hash;
    }

    public boolean equals(Token obj) {
        if (null == obj) {
            return false;
        }
        return Value.equals(obj.Value) && TokenType.equals(obj.TokenType);
    }
    @Override
    public String toString(){
        return String.format("%1:%2", TokenType, Value);
    }
}
