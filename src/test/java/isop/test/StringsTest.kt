/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isop.test

import isop.infrastructure.Strings
import org.junit.Test
import org.junit.Assert.*

/**
 *
 * @author mathieu
 */
class StringsTest {
    @Test
    fun Test() {
        val expected = arrayOf("a", "beta", "gamma")
        assertArrayEquals(expected, Strings.split("a,beta,gamma", ','))
        //assert Strings.split(
    }
}
