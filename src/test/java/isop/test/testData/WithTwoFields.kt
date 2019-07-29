package isop.test.testData

/**
 * Created by mathieu.
 */
class WithTwoFields(var first: Int) {
    var second: String

    init {
        second = "V$first"
    }
}
