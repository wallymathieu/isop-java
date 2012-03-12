package jisop;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author mathieu
 */
public class PeekCollection<T> extends LinkedList<T> {

    public PeekCollection(Collection<T> col){
        super(col);
    }
    private int _currentIndex = -1;

    public T current() {
        if (_currentIndex < this.size()) {
            return this.get(_currentIndex);
        }
        throw new RuntimeException("out of range");
    }

    public boolean hasMore() {
        return _currentIndex + 1 < size();
    }

    public T next() {
        _currentIndex++;
        return current();
    }

    public T peekNext() {
        int idx = _currentIndex + 1;
        return idx < size() ? get(idx) : null;
    }
}
