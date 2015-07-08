package jisop.infrastructure;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by mathieu.
 */
public class Activator {

    public static Object createInstance(Class<?> type, Function<Class, Object> getInstance){
        try {
            for (Constructor c: type.getConstructors()) {
                return c.newInstance(Arrays.asList( c.getParameterTypes() )
                        .stream()
                        .map(t->getInstance.apply(t))
                        .toArray());
            }
            throw new RuntimeException("Could not find constructor");
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Activator.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Class: " + type.getName(), ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Activator.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Class: " + type.getName(), ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Activator.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Class: " + type.getName(), ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Activator.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Class: " + type.getName(), ex);
        }

    }
}
