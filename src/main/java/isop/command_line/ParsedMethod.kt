/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isop.command_line

import isop.command_line.parse.ParsedArguments
import isop.domain.Configuration
import isop.domain.Method

import java.lang.reflect.InvocationTargetException
import java.util.function.Function
import java.util.stream.Stream

/**
 *
 * @author mathieu
 */
class ParsedMethod(parsedArguments: ParsedArguments,
                   var recognizedClass: Class<*>,
                   var recognizedAction: Method,
                   var recognizedActionParameters: Array<Any?>,
                   private val _typeContainer: Function<Class<*>, Any?>,
                   private val configuration: Configuration) : ParsedArguments(parsedArguments) {

    override fun invoke(): Stream<String> {
        val instance = _typeContainer.apply(this.recognizedClass)

        var retval: Any? = null
        try {
            retval = recognizedAction.invoke(instance!!, recognizedActionParameters)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException(e)
        }
        if (retval==null)
            return Stream.empty()
        return configuration.formatter.format(retval)
    }
}
