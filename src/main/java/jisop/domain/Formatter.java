package jisop.domain;

import java.util.stream.Stream;

/**
 * Created by mathieu.
 */
public interface Formatter {
    Stream<String> Format(Object value);
}
