/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isop

import isop.test.*
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 *
 * @author mathieu
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(ArgumentParserTest::class, ArgumentLexerTest::class, ListsTest::class, StringsTest::class, TableFormatterTest::class, StringFormatterTest::class, AlternativeApiTest::class)//ConfigurationTest.class
object IsopTestSuite {


}
