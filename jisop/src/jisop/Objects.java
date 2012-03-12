package jisop;

/**
 *
 * @author mathieu
 */
class Objects {

    static int hashCode(Object Value) {
        if (null==Value) return 0;
        return Value.hashCode();
    }

    static boolean equals(Object Value, Object Value0) {
        if (Value==null) return Value0==null;
        return Value.equals(Value0);
    }
    
}
