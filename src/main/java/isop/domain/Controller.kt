package isop.domain

import isop.help.HelpController
import isop.infrastructure.Objects

import java.util.Arrays
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Created by mathieu.
 */
class Controller(val type: Class<*>, val ignoreGlobalUnMatchedParameters: Boolean?) {
    val name: String

    val controllerActionMethods: Collection<Method>
        get() = _GetControllerActionMethods(type).map { m ->
            val mm = Method(m)
            mm.controller = this
            mm
        }.toList()

    // Is there a better way to do this?
    val isHelp: Boolean
        get() = type == HelpController::class.java


    init {
        this.name = controllerName(type)
    }

    private fun controllerName(type: Class<*>): String {
        val p = Pattern.compile(Conventions.ControllerName + "$", Pattern.CASE_INSENSITIVE)
        val m = p.matcher(type.simpleName)
        return m.replaceAll("")
    }

    private fun _GetControllerActionMethods(type: Class<*>): Collection<java.lang.reflect.Method> {
        return getOwnPublicMethods(type)
                .filter { m -> !m.name.equals(Conventions.Help, ignoreCase = true) }
                .toList()
    }

    private fun getOwnPublicMethods(type: Class<*>): Collection<java.lang.reflect.Method> {
        return Arrays.asList<java.lang.reflect.Method>(*type.getMethods())
                .filter { m -> m.getDeclaringClass() != Any::class.java }
                .filter { m -> !m.getName().startsWith("get") || !m.getName().startsWith("set") }
    }

    fun recognize(controllerName: String): Boolean {
        return name.equals(controllerName, ignoreCase = true)
    }

    fun recognize(controllerName: String, actionName: String): Boolean {
        return name.equals(controllerName, ignoreCase = true) && getMethod(actionName) != null
    }

    fun getMethod(name: String): Method? {
        return controllerActionMethods
                .find { m -> m.name.equals(name, ignoreCase = true) }
    }

    override fun equals(obj: Any?): Boolean {
        return obj is Controller && Equals((obj as Controller?)!!)
    }

    fun Equals(obj: Controller): Boolean {
        return type == obj.type
    }

    override fun hashCode(): Int {
        return Objects.hashCode(type)
    }
}
