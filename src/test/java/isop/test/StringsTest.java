/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isop.test;

import isop.infrastructure.Strings;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mathieu
 */
public class StringsTest {
    @Test
    public void Test(){
        String[] expected = { "a", "beta","gamma"};
        assertArrayEquals(expected, Strings.split("a,beta,gamma", ','));
        //assert Strings.split(
    }
}
