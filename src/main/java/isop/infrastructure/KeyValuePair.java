package isop.infrastructure;

/**
 * Created by mathieu.
 */
public class KeyValuePair<TKey,TValue> {
    public final TKey key;
    public final TValue value;
    public KeyValuePair(TKey key,TValue value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s:%s", key, value);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.key);
        hash = 79 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null &&
                obj instanceof KeyValuePair &&
                equals((KeyValuePair) obj);
    }
    public boolean equals(KeyValuePair obj) {
        return obj != null &&
                Objects.equals(obj.key, key) &&
                Objects.equals(obj.value, value);
    }
}
