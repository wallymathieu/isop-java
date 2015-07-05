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

    public TypeConversionFailedException(){}

    public TypeConversionFailedException(String message) {
        super(message);
    }
    public TypeConversionFailedException(String message, Exception e) {
        super(message,e);
    }
}
