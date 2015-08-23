/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isop;

import isop.infrastructure.KeyValuePair;

/**
 *
 * @author mathieu
 */
public class TypeConversionFailedException extends RuntimeException {
    public TypeConversionFailedException(String message) {
        super(message);
    }

    public TypeConversionFailedException(String message, Exception e) {
        super(message,e);
    }

    public TypeConversionFailedException(String message, Exception e, KeyValuePair<String, String> arg1, Class type) {
        super(String.format("%s where %s: %s to %s", message, arg1.key, arg1.value, type.getSimpleName()),e);
    }
}
