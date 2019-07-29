package isop.domain

import java.util.HashMap
import java.util.function.Function
import java.util.logging.Level
import java.util.logging.Logger

/**
 *
 * @author mathieu
 */
class TypeContainer : Function<Class<*>, Any?> {

    private val instances: HashMap<Class<*>, Any> = HashMap()
    private var factory: Function<Class<*>, Any>? = null

    fun add(t: Class<*>, instance: Any) {
        instances[t] = instance
    }

    override fun apply(c: Class<*>): Any? {
        if (!instances.containsKey(c)) {
            if (factory != null) {
                instances[c] = factory!!.apply(c)
            } else {
                try {
                    instances[c] = c.newInstance()
                } catch (ex: InstantiationException) {
                    Logger.getLogger(TypeContainer::class.java.name).log(Level.SEVERE, null, ex)
                    throw RuntimeException(ex)
                } catch (ex: IllegalAccessException) {
                    Logger.getLogger(TypeContainer::class.java.name).log(Level.SEVERE, null, ex)
                    throw RuntimeException(ex)
                }

            }
        }
        return instances[c]
    }

    fun setFactory(objectFactory: Function<Class<*>, Any>) {
        this.factory = objectFactory
    }
}
