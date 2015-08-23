package isop.infrastructure;

import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author mathieu
 */
public class PeekCollection<T> {
    private LinkedList<T> col;
    public PeekCollection(Collection<T> col){
        this.col = new LinkedList<>(col);
    }
    private int _currentIndex = -1;

    public T current() {
        if (_currentIndex < col.size()) {
            return col.get(_currentIndex);
        }
        throw new RuntimeException("out of range");
    }

    public boolean hasMore() {
        return _currentIndex + 1 < col.size();
    }

    public T next() {
        _currentIndex++;
        return current();
    }

    public T peek() {
        int idx = _currentIndex + 1;
        return idx < col.size() ? col.get(idx) : null;
    }
}
