/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
public class ParsedMethod extends ParsedArguments {

    ObjectFactory factory;
    Method recognizedAction;
    List<Object> recognizedActionParameters;
    Object recognizedInstance;

    ParsedMethod(ParsedArguments parsedArguments) {
        super(parsedArguments);
    }

    public void Invoke(OutputStream cout) {
        OutputStreamWriter writer = new OutputStreamWriter(cout);
        Object retval;
        try {
            retval = recognizedAction.invoke(recognizedInstance, recognizedActionParameters.toArray());
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ParsedMethod.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ParsedMethod.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ParsedMethod.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
        if (retval == null) {
            return;
        }
        try {
            if (retval instanceof String) {

                writer.write((String) retval);
            } else if (retval instanceof Collection) {
                for (Object item : (Collection) retval) {
                    writer.write((String) retval);
                }
            } else {
                writer.write(retval.toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(ParsedMethod.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
}
