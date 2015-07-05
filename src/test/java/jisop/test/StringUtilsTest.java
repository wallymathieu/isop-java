/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop.test;

import jisop.infrastructure.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mathieu
 */
public class StringUtilsTest {
    @Test
    public void Test(){
        String[] expected = { "a", "beta","gamma"};
        assertArrayEquals(expected, StringUtils.split("a,beta,gamma", ','));
        //assert StringUtils.split(
    }
}
