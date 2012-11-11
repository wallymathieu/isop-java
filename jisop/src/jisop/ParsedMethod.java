/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop;

import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * @author mathieu
 */
public class ParsedMethod extends ParsedArguments{
    ObjectFactory factory;
    Method recognizedAction;
    List<Object> recognizedActionParameters;
    Object recognizedInstance;
    
    ParsedMethod(ParsedArguments parsedArguments) {
       super(parsedArguments);
    }
    
}
