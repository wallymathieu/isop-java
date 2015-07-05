/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop.tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author mathieu
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        jisop.tests.ArgumentParserTests.class,
        jisop.tests.ArgumentLexerTests.class,
        jisop.tests.ListUtilsTests.class,
        jisop.tests.StringUtilsTests.class})
public class JisopTestSuite {

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }
    
}
