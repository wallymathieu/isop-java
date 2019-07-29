package isop

import isop.api.ControllerExpression
import isop.command_line.ControllerRecognizer
import isop.command_line.lex.ArgumentLexer
import isop.command_line.parse.ArgumentParameter
import isop.command_line.parse.ArgumentParser
import isop.command_line.parse.ArgumentWithOptions
import isop.command_line.parse.ParsedArguments
import isop.domain.*
import isop.domain.Formatter
import isop.help.HelpController
import isop.help.HelpForArgumentWithOptions
import isop.help.HelpForControllers
import java.util.*
import java.util.function.Consumer
import java.util.function.Function

/**
 *
 * @author mathieu
 */
class AppHostBuilder {
    private val _configuration = Configuration()
    var _allowInferParameter = true // TODO:FIX
    private val _container = TypeContainer()
    private var _helpController: HelpController? = null
    private var _helpForControllers: HelpForControllers? = null
    private var _helpForArgumentWithOptions: HelpForArgumentWithOptions? = null

    val controllerRecognizers: Map<Class<*>, () -> ControllerRecognizer>
        get() {
            if (_configuration.recognizesHelp && !_configuration.recognizes
                            .any { c -> c.type == HelpController::class.java }) {
                recognize(helpController!!, true)
            }

            return _configuration.recognizes.map{ c-> Pair(c.type, this.getRecognizerSupplier(c))}.toMap()
        }

    private val helpController: Any?
        get() {
            if (_helpController == null && _configuration.recognizesHelp) {
                _helpForControllers = HelpForControllers(_configuration.recognizes, _container)
                _helpForArgumentWithOptions = HelpForArgumentWithOptions(globalParameters)
                _helpController = HelpController(_helpForArgumentWithOptions, _helpForControllers)
            }
            return _helpController
        }

    val globalParameters: Collection<Argument>
        get() = _configuration.fields.map { p ->
            ArgumentWithOptions(
                    ArgumentParameter.parse(p.name),
                    p.action,
                    p.required,
                    p.description)
        }.toList()

    @JvmOverloads
    fun parameter(argument: String,
                  required: Boolean = false,
                  description: String? = null,
                  action: Consumer<String>? = null): AppHostBuilder {
        var f = Field(argument, action, required, description)
        _configuration.fields.add(f)
        return this
    }


    fun formatObjectsAsTable(): AppHostBuilder {
        _configuration.formatter = TableFormatter()
        return this
    }

    fun setFormatter(formatter: Formatter): AppHostBuilder {
        _configuration.formatter = formatter
        return this
    }

    fun parse(arg: Array<String>): ParsedArguments {
        val argumentParser = ArgumentParser(globalParameters, _allowInferParameter)
        val lexed = ArgumentLexer.lex(arg)
        val parsedArguments = argumentParser.parse(lexed, Arrays.asList(*arg))
        val controllerRecognizer = controllerRecognizers
                .map { it.value() }
                .find { it.recognize(arg) }
        if (null != controllerRecognizer) {
            return controllerRecognizer.parseArgumentsAndMerge(arg,
                    parsedArguments)
        }
        parsedArguments.assertFailOnUnMatched()
        return parsedArguments
    }

    @JvmOverloads
    fun recognizeClass(arg: Class<*>, ignoreGlobalUnMatchedParameters: Boolean = false): AppHostBuilder {
        _configuration.recognizes.add(Controller(arg, ignoreGlobalUnMatchedParameters))
        return this
    }

    @JvmOverloads
    fun recognize(arg: Any, ignoreGlobalUnMatchedParameters: Boolean = false): AppHostBuilder {
        _configuration.recognizes.add(Controller(arg.javaClass, ignoreGlobalUnMatchedParameters))
        _container.add(arg.javaClass, arg)
        return this
    }

    fun help(): String {
        throw RuntimeException("Not implemented")
    }

    private fun getRecognizerSupplier(c: Controller): () -> ControllerRecognizer {
        return { ControllerRecognizer(c, _configuration, _container, _allowInferParameter) }
    }

    fun setFactory(objectFactory: Function<Class<*>, Any>): AppHostBuilder {
        _container.setFactory(objectFactory)
        return this
    }

    fun controller(controllerName: String): ControllerExpression {
        return ControllerExpression(controllerName, this)
    }
}
