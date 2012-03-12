package jisop.tests;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import jisop.ArgumentLexer;
import jisop.PeekCollection;
import jisop.Token;
import jisop.TokenType;
import org.junit.*;

/**
 *
 * @author mathieu
 */
public class ArgumentLexerTests {

    public ArgumentLexerTests() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //

    @Test
    public void It_can_tokenize_simple_argument() {
        ArgumentLexer lexer = ArgumentLexer.Lex(new String[]{"argument"});
        Assert.assertArrayEquals(new Token[]{
                    new Token("argument", TokenType.Argument, 0)},
                lexer.toArray());
    }

    @Test
    public void It_can_tokenize_parameter() {
        ArgumentLexer lexer = ArgumentLexer.Lex(new String[]{"--parameter"});

        Assert.assertArrayEquals(new Token[]{
                    new Token("parameter", TokenType.Parameter, 0)},
                lexer.toArray());
    }

    @Test
    public void It_can_tokenize_parameter2() {
        ArgumentLexer lexer = ArgumentLexer.Lex(new String[]{"/parameter"});
        Assert.assertArrayEquals(new Token[]{
                    new Token("parameter", TokenType.Parameter, 0)},
                lexer.toArray());
    }

    @Test
    public void It_can_tokenize_parametervalue() {
        ArgumentLexer lexer = ArgumentLexer.Lex(new String[]{"--parameter", "parametervalue"});
        Assert.assertArrayEquals(new Token[]{
                    new Token("parameter", TokenType.Parameter, 0),
                    new Token("parametervalue", TokenType.ParameterValue, 1)},
                lexer.toArray());
    }

    @Test
    public void It_can_tokenize_parametervalue2() {
        ArgumentLexer lexer = ArgumentLexer.Lex(new String[]{"--parameter=parametervalue"});
        Assert.assertArrayEquals(new Token[]{
                    new Token("parameter", TokenType.Parameter, 0),
                    new Token("parametervalue", TokenType.ParameterValue, 1)},
                lexer.toArray());

    }

    @Test
    public void It_can_peek_tokenized_value() {

        PeekCollection<Token> lexer = new PeekCollection<Token>(ArgumentLexer.Lex(new String[]{
                    "--parameter=parametervalue", "argument"}));
        lexer.next();
        Token first = lexer.peekNext();
        Assert.assertEquals(new Token("parametervalue", TokenType.ParameterValue, 1),
                first);

        Assert.assertEquals(first,
                lexer.next());
        Assert.assertEquals(new Token("argument",
                TokenType.Argument, 2),
                lexer.peekNext());
    }
}
