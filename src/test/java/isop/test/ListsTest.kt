/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isop.test

import isop.infrastructure.Lists
import org.junit.*

import java.util.Arrays

import org.junit.Assert.*

/**
 *
 * @author mathieu
 */
class ListsTest {

    @Test
    fun Union() {
        val expected = arrayOf(1, 2, 3)
        val set1 = arrayOf(1, 2)
        val set2 = arrayOf(2, 3)
        val result = Lists.union(Arrays.asList(*set1), Arrays.asList(*set2)).toTypedArray()
        assertArrayEquals(expected, result)
    }

    @Test
    fun Intersect() {
        val expected = arrayOf(2)
        val set1 = arrayOf(1, 2)
        val set2 = arrayOf(2, 3)
        val result = Lists.intersect(set1, set2).toTypedArray()
        assertArrayEquals(expected, result)
    }

}
