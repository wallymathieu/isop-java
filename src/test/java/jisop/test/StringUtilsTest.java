/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import jisop.*;
/**
 *
 * @author mathieu
 */
public class StringUtilsTest {
    @Test
    public void Test(){
        String[] expected = { "a", "beta","gamma"};
        assertArrayEquals(expected, StringUtils.split("a,beta,gamma",','));
        //assert StringUtils.split(
    }
}
