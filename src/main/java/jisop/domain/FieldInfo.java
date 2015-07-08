package jisop.domain;

import java.util.function.Consumer;

/**
 * Created by mathieu.
 *
 * The reasoning behind this class is to be able to handle getters and setters in the same way as fields.
 */
public abstract class FieldInfo {

    public String name;
    public Class propertyType;
    public boolean looksRequired;

    public abstract void setValue(Object obj, Object value) throws IllegalArgumentException, IllegalAccessException;
}