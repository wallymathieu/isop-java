/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop;

/**
 *
 * @author mathieu
 */
public class TypeConversionFailedException extends RuntimeException {

    TypeConversionFailedException(){}
    
    TypeConversionFailedException(String message) {
        super(message);
    }
    TypeConversionFailedException(String message, Exception e) {
        super(message,e);
    }
}
