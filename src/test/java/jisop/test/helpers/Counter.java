package jisop.test.helpers;

/**
 * Created by mathieu.
 */
public class Counter {
    private int count;

    public int getCount() {
        return count;
    }
    public int next(){
        return ++count;
    }
}
