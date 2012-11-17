/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop.tests;

import org.junit.Test;
import static org.junit.Assert.*;
import jisop.*;
/**
 *
 * @author mathieu
 */
public class StringUtilsTests {
    @Test
    public void Test(){
        String[] expected = { "a", "beta","gamma"};
        assertArrayEquals(expected, StringUtils.split("a,beta,gamma",','));
        //assert StringUtils.split(
    }
}
