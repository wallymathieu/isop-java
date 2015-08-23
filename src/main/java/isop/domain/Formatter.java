package isop.domain;

import java.util.stream.Stream;

/**
 * Created by mathieu.
 */
public interface Formatter {
    Stream<String> format(Object value);
}
