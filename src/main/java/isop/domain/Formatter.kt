package isop.domain

import java.util.stream.Stream

/**
 * Created by mathieu.
 */
interface Formatter {
    fun format(value: Any): Stream<String>
}
