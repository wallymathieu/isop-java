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
public final class Activator {
    private Activator() {
    }
    public static Object createInstance(Class<?> type, Function<Class, Object> getInstance) {
        try {
             return Arrays.asList(type.getConstructors()).stream()
                     .map(c -> create(c, getInstance))
                     .findFirst()
                     .orElseThrow(()->new RuntimeException("Could not find constructor"));
        } catch (IllegalArgumentException ex) {
            return Fail(type, ex);
        }
    }

    private static Object create(final Constructor<?> c, final Function<Class, Object> getInstance) {
        try {
            return c.newInstance(Arrays.asList(c.getParameterTypes())
                    .stream()
                    .map(getInstance::apply)
                    .toArray());
        } catch (InstantiationException|IllegalAccessException|InvocationTargetException e) {
            return Fail(c.getDeclaringClass(), e);
        }
    }

    private static Object Fail(Class t, Exception e) {
        Logger.getLogger(Activator.class.getName()).log(Level.SEVERE, null, e);
        throw new RuntimeException("Class: " + t.getName(), e);
    }
}
