package isop.test

import java.util.AbstractMap
import java.util.function.Function
import java.util.regex.*
import java.util.*

/**
 * Created by mathieu.
 */
open class Base {
    companion object {
        private val toKv = Pattern.compile("\\[(#?\\w*), (\\w*)\\]")
        fun dictionaryDescriptionToKv(input: String, convert: Function<String, Any>): List<AbstractMap.SimpleEntry<String, Any>> {
            val list = ArrayList<AbstractMap.SimpleEntry<String, Any>>()
            val m = toKv.matcher(input)
            while (m.find()) {
                list.add(toSimpleEntry(m.group(1), convert.apply(m.group(2))))
            }
            return list
        }

        fun toSimpleEntry(key: String, value: Any): AbstractMap.SimpleEntry<String, Any> {
            return AbstractMap.SimpleEntry(key, value)
        }
    }
}
