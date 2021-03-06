/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isop.test;
import isop.infrastructure.Lists;
import org.junit.*;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 *
 * @author mathieu
 */
public class ListsTest {

    @Test
    public void Union() {
        Integer[] expected = {1,2,3};
        Integer[] set1= {1,2};
        Integer[] set2={2,3};
        Integer[] result = Lists.union(Arrays.asList(set1), Arrays.asList(set2)).toArray(new Integer[0]);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void Intersect() {
        Integer[] expected = {2};
        Integer[] set1={1,2};
        Integer[] set2={2,3};
        Integer[] result = Lists.intersect(set1, set2).toArray(new Integer[0]);
        assertArrayEquals(expected, result);
    }

}
