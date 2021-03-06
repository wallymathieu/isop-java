package isop.test;



/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import isop.command_line.lex.ArgumentLexer;
import isop.infrastructure.PeekCollection;
import isop.command_line.lex.Token;
import isop.command_line.lex.TokenType;
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
    public void It_can_tokenize_parameter_value() {
        ArgumentLexer lexer = ArgumentLexer.lex(new String[]{"--parameter", "parametervalue"});
        assertArrayEquals(new Token[]{
                    new Token("parameter", TokenType.PARAMETER, 0),
                    new Token("parametervalue", TokenType.PARAMETER_VALUE, 1)},
                lexer.toArray());
    }

    @Test
    public void It_can_tokenize_parameter_value_slash_and_equals() {
        ArgumentLexer lexer = ArgumentLexer.lex(new String[]{"/parameter=parametervalue"});
        assertArrayEquals(new Token[]{
                    new Token("parameter", TokenType.PARAMETER, 0),
                    new Token("parametervalue", TokenType.PARAMETER_VALUE, 1)},
                lexer.toArray());

    }

    @Test
    public void It_can_tokenize_parameter_value_minus_minus_and_equals() {
        ArgumentLexer lexer = ArgumentLexer.lex(new String[]{"--parameter=parametervalue"});
        assertArrayEquals(new Token[]{
                        new Token("parameter", TokenType.PARAMETER, 0),
                        new Token("parametervalue", TokenType.PARAMETER_VALUE, 1)},
                lexer.toArray());
    }

    @Test
    public void It_can_peek_tokenized_value() {

        PeekCollection<Token> lexer = new PeekCollection<>(ArgumentLexer.lex(new String[]{
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
