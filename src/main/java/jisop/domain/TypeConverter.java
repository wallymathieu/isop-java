package jisop.domain;

/**
 *
 * @author mathieu
 */
public interface TypeConverter {
    public Object convert(Class cls, String value);
}
