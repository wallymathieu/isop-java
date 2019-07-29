package isop.test


/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import isop.command_line.lex.ArgumentLexer
import isop.infrastructure.PeekCollection
import isop.command_line.lex.Token
import isop.command_line.lex.TokenType
import org.junit.*
import org.junit.Assert.*

/**
 *
 * @author mathieu
 */
class ArgumentLexerTest {


    @Test
    fun It_can_tokenize_simple_argument() {
        val lexer = ArgumentLexer.lex(arrayOf("argument"))
        assertArrayEquals(arrayOf(Token("argument", TokenType.ARGUMENT, 0)),
                lexer.toTypedArray())
    }

    @Test
    fun It_can_tokenize_parameter_minus_minus() {
        val lexer = ArgumentLexer.lex(arrayOf("--parameter"))

        assertArrayEquals(arrayOf(Token("parameter", TokenType.PARAMETER, 0)),
                lexer.toTypedArray())
    }

    @Test
    fun It_can_tokenize_parameter_slash() {
        val lexer = ArgumentLexer.lex(arrayOf("/parameter"))
        assertArrayEquals(arrayOf(Token("parameter", TokenType.PARAMETER, 0)),
                lexer.toTypedArray())
    }

    @Test
    fun It_can_tokenize_parameter_value() {
        val lexer = ArgumentLexer.lex(arrayOf("--parameter", "parametervalue"))
        assertArrayEquals(arrayOf(Token("parameter", TokenType.PARAMETER, 0), Token("parametervalue", TokenType.PARAMETER_VALUE, 1)),
                lexer.toTypedArray())
    }

    @Test
    fun It_can_tokenize_parameter_value_slash_and_equals() {
        val lexer = ArgumentLexer.lex(arrayOf("/parameter=parametervalue"))
        assertArrayEquals(arrayOf(Token("parameter", TokenType.PARAMETER, 0), Token("parametervalue", TokenType.PARAMETER_VALUE, 1)),
                lexer.toTypedArray())

    }

    @Test
    fun It_can_tokenize_parameter_value_minus_minus_and_equals() {
        val lexer = ArgumentLexer.lex(arrayOf("--parameter=parametervalue"))
        assertArrayEquals(arrayOf(Token("parameter", TokenType.PARAMETER, 0), Token("parametervalue", TokenType.PARAMETER_VALUE, 1)),
                lexer.toTypedArray())
    }

    @Test
    fun It_can_peek_tokenized_value() {

        val lexer = PeekCollection(ArgumentLexer.lex(arrayOf("--parameter=parametervalue", "argument")))
        lexer.next()
        val first = lexer.peek()
        assertEquals(Token("parametervalue", TokenType.PARAMETER_VALUE, 1),
                first)

        assertEquals(first,
                lexer.next())
        assertEquals(Token("argument",
                TokenType.ARGUMENT, 2),
                lexer.peek())
    }
}
