package jisop;

/**
 *
 * @author mathieu
 */
public class MissingArgumentException extends RuntimeException {

    public final String[] unmatched;

    public MissingArgumentException(String msg, String[] unmatched) {
        super(msg);
        this.unmatched = unmatched;
    }
}
