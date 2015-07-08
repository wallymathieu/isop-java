package jisop.command_line.lex;


/**
 *
 * @author mathieu
 */
public class Token {

    public String value;
    public TokenType tokenType;
    /// <summary>
    /// the index in the argument array
    /// </summary>
    public int index;

    public Token(String value, TokenType tokenType, int index) {
        this.value = value;
        this.tokenType = tokenType;
        this.index = index;
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
        hash = 79 * hash + this.value.hashCode();
        hash = 79 * hash + (this.tokenType != null ? this.tokenType.hashCode() : 0);
        return hash;
    }

    public boolean equals(Token obj) {
        if (null == obj) {
            return false;
        }
        return value.equals(obj.value) && tokenType.equals(obj.tokenType);
    }
    @Override
    public String toString(){
        return String.format("%1s: '%2s'", tokenType.toString(), value.toString());
    }
}
