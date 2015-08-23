package isop.test.helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mathieu.
 */
public class MapBuilder<TKey,TValue> {
    private final Map<TKey,TValue> map;
    public MapBuilder(){
        map = new HashMap();
    }
    public MapBuilder<TKey,TValue> put(TKey key, TValue value){
        map.put(key, value);
        return this;
    }
    public Map<TKey,TValue> map(){
        return map;
    }
}
