package isop.infrastructure;

/**
 *
 * @author mathieu
 */
public final class Objects {
    private Objects(){}
    public static int hashCode(Object Value) {
        if (null==Value) return 0;
        return Value.hashCode();
    }

    public static boolean equals(Object Value, Object Value0) {
        if (Value==null) return Value0==null;
        return Value.equals(Value0);
    }
    
}
