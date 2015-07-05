/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop.domain;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 *
 * @author mathieu
 */
public interface ObjectFactory extends Function<Class,Object> {
}
