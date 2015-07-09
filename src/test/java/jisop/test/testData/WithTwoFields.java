package jisop.test.testData;

/**
 * Created by mathieu.
 */
public class WithTwoFields {
    public WithTwoFields(int value)
    {
        first = value;
        second = "V"+value;
    }
    public int first;
    public String second;
}
