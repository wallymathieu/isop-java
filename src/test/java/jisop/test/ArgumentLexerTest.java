package jisop.test;



/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import jisop.command_line.lex.ArgumentLexer;
import jisop.infrastructure.PeekCollection;
import jisop.command_line.lex.Token;
import jisop.command_line.lex.TokenType;
import org.junit.*;
import static org.junit.Assert.*;
/**
 *
 * @author mathieu
 */
public class ArgumentLexerTest {

    public ArgumentLexerTest() {
    }


    @Test
    public void It_can_tokenize_simple_argument() {
        ArgumentLexer lexer = ArgumentLexer.lex(new String[]{"argument"});
        assertArrayEquals(new Token[]{
                    new Token("argument", TokenType.ARGUMENT, 0)},
                lexer.toArray());
    }

    @Test
    public void It_can_tokenize_parameter_minus_minus() {
        ArgumentLexer lexer = ArgumentLexer.lex(new String[]{"--parameter"});

        assertArrayEquals(new Token[]{
                    new Token("parameter", TokenType.PARAMETER, 0)},
                lexer.toArray());
    }

    @Test
    public void It_can_tokenize_parameter_slash() {
        ArgumentLexer lexer = ArgumentLexer.lex(new String[]{"/parameter"});
        assertArrayEquals(new Token[]{
                    new Token("parameter", TokenType.PARAMETER, 0)},
                lexer.toArray());
    }

    @Test
    public void It_can_tokenize_parametervalue() {
        ArgumentLexer lexer = ArgumentLexer.lex(new String[]{"--parameter", "parametervalue"});
        assertArrayEquals(new Token[]{
                    new Token("parameter", TokenType.PARAMETER, 0),
                    new Token("parametervalue", TokenType.PARAMETER_VALUE, 1)},
                lexer.toArray());
    }

    @Test
    public void It_can_tokenize_parametervalue_slash_and_equals() {
        ArgumentLexer lexer = ArgumentLexer.lex(new String[]{"/parameter=parametervalue"});
        assertArrayEquals(new Token[]{
                    new Token("parameter", TokenType.PARAMETER, 0),
                    new Token("parametervalue", TokenType.PARAMETER_VALUE, 1)},
                lexer.toArray());

    }

    @Test
    public void It_can_tokenize_parametervalue_minus_minus_and_equals() {
        ArgumentLexer lexer = ArgumentLexer.lex(new String[]{"--parameter=parametervalue"});
        assertArrayEquals(new Token[]{
                        new Token("parameter", TokenType.PARAMETER, 0),
                        new Token("parametervalue", TokenType.PARAMETER_VALUE, 1)},
                lexer.toArray());
    }

    @Test
    public void It_can_peek_tokenized_value() {

        PeekCollection<Token> lexer = new PeekCollection<Token>(ArgumentLexer.lex(new String[]{
                    "--parameter=parametervalue", "argument"}));
        lexer.next();
        Token first = lexer.peek();
        assertEquals(new Token("parametervalue", TokenType.PARAMETER_VALUE, 1),
                first);

        assertEquals(first,
                lexer.next());
        assertEquals(new Token("argument",
                TokenType.ARGUMENT, 2),
                lexer.peek());
    }
}
