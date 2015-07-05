/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author mathieu
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ArgumentParserTest.class, ArgumentLexerTest.class,
    ListUtilsTest.class, StringUtilsTest.class})
public class JisopTestSuite {

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }
    
}
