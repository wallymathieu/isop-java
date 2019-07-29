package isop

/**
 *
 * @author mathieu
 */
class MissingArgumentException(msg: String, val unmatched: Array<String?>) : RuntimeException(msg)
