package isop.domain

import java.util.Arrays
import java.util.TreeSet

/**
 * Created by mathieu.
 */
object Conventions {
    var ControllerName = "controller"

    var ConfigurationName = IgnoreCase(arrayOf("isopconfiguration"))

    var Help = "help"

    var Index = "index"

    private fun IgnoreCase(strings: Array<String>): Set<String> {
        val s = TreeSet(String.CASE_INSENSITIVE_ORDER)
        s.addAll(Arrays.asList(*strings))
        return s
    }

    fun equalsHelp(value: String): Boolean {
        return Help == value.toLowerCase()
    }

    fun equalsIndex(value: String): Boolean {
        return Index == value.toLowerCase()
    }
}
