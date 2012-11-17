/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop.tests;
import java.util.Collection;
import jisop.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author mathieu
 */
public class ListUtilsTests {

    @Test
    public void Union() {
        Integer[] expected = {1,2,3};
        Integer[] set1={1,2};
        Integer[] set2={2,3};
        Integer[] result = ListUtils.union(set1, set2).toArray(new Integer[0]);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void Intersect() {
        Integer[] expected = {2};
        Integer[] set1={1,2};
        Integer[] set2={2,3};
        Integer[] result = ListUtils.intersect(set1, set2).toArray(new Integer[0]);
        assertArrayEquals(expected, result);
    }

}
