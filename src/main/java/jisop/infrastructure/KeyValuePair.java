package jisop.infrastructure;

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
        if (obj==null){
            return false;
        }
        if (obj instanceof KeyValuePair){
            return equals((KeyValuePair) obj);
        }
        return false;
    }
    public boolean equals(KeyValuePair obj) {
        if (obj==null){
            return false;
        }
        if (!Objects.equals(obj.key, key)){
            return false;
        }
        if (!Objects.equals(obj.value, value)){
            return false;
        }
        return true;
    }
}
