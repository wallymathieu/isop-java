package isop.command_line

import isop.MissingArgumentException
import isop.command_line.lex.ArgumentLexer
import isop.command_line.lex.Token
import isop.command_line.parse.ArgumentParameter
import isop.command_line.parse.ArgumentParser
import isop.command_line.parse.ArgumentWithOptions
import isop.command_line.parse.ParsedArguments
import isop.domain.*

import java.util.Arrays
import java.util.function.Function
import java.util.stream.Collectors

/**
 *
 * @author mathieu
 */
class ControllerRecognizer(private val _controller: Controller, private val _configuration: Configuration, private val _typeContainer: Function<Class<*>, Any?>, private val _allowInferParameter: Boolean) {

    fun parseArgumentsAndMerge(actionName: String, arg: Map<String, String>, parsedArguments: ParsedArguments): ParsedArguments {
        val methodInfo = _controller.getMethod(actionName)
        val argumentRecognizers = methodInfo!!.arguments

        val parser = ArgumentParser(argumentRecognizers, _allowInferParameter)
        val parsedMethodArguments = parser.parse(arg)
        val parsedMethod = parse(methodInfo, parsedMethodArguments)
        val merged = parsedArguments.merge(parsedMethod)
        if ((!_controller.ignoreGlobalUnMatchedParameters!!)!!)
            merged.assertFailOnUnMatched()
        return merged
    }

    fun parseArgumentsAndMerge(arg: Array<String>, parsedArguments: ParsedArguments): ParsedArguments {
        val parsedMethod = parse(arg)
        // Inferred ordinal arguments should not be recognized twice
        parsedArguments.recognizedArguments = parsedArguments.recognizedArguments
                .filter { argopts ->
                    !parsedMethod.recognizedArguments.stream()
                            .anyMatch { pargopt -> pargopt.index == argopts.index && argopts.inferredOrdinal }
                }
                .toList()
        val merged = parsedArguments.merge(parsedMethod)
        if ((!_controller.ignoreGlobalUnMatchedParameters!!)!!)
            merged.assertFailOnUnMatched()
        return merged
    }

    fun recognize(arg: Array<String>): Boolean {
        val lexed = RewriteLexedTokensToSupportHelpAndIndex.rewrite(ArgumentLexer.lex(arg))
        return null != findMethodInfo(lexed)
    }

    fun getRecognizers(method: String): List<Argument> {
        return _controller.getMethod(method)!!.arguments
    }

    /// <summary>
    /// Note that in order to register a converter you can use:
    /// TypeDescriptor.AddAttributes(typeof(AType), new TypeConverterAttribute(typeof(ATypeConverter)));
    /// </summary>
    /// <param name="arg"></param>
    /// <returns></returns>
    fun parse(arg: Array<String>): ParsedMethod {
        val lexed = RewriteLexedTokensToSupportHelpAndIndex.rewrite(ArgumentLexer.lex(arg))

        val methodInfo = findMethodInfo(lexed)

        val argumentRecognizers = methodInfo!!.arguments.toMutableList()
        argumentRecognizers.addAll(0, Arrays.asList<ArgumentWithOptions>(
                ArgumentWithOptions(ArgumentParameter.parse("#0" + _controller.name)).type(String::class.java).required(true),
                ArgumentWithOptions(ArgumentParameter.parse("#1" + methodInfo.name)).required(false).type(String::class.java))
        )

        val parser = ArgumentParser(argumentRecognizers, _allowInferParameter)
        val parsedArguments = parser.parse(lexed, Arrays.asList(*arg))

        return parse(methodInfo, parsedArguments)
    }

    private fun findMethodInfo(arg: List<Token>): Method? {
        if (arg.size < 2) {
            return null
        }
        val foundClassName = _controller.name.equals(arg[0].value, ignoreCase = true)
        if (foundClassName) {
            val methodName = arg[1].value
            return FindMethodAmongLexedTokens.findMethod(
                    _controller.controllerActionMethods,
                    methodName, arg)
        }
        return null
    }

    fun parse(methodInfo: Method, parsedArguments: ParsedArguments): ParsedMethod {
        val unMatchedRequiredArguments = parsedArguments.UnMatchedRequiredArguments()
        if (unMatchedRequiredArguments.stream().anyMatch { a -> true }) {
            throw MissingArgumentException("Missing arguments",
                    unMatchedRequiredArguments.map { a -> a.name }.toTypedArray())
        }
        val convertArgument = ConvertArgumentsToParameterValue(_configuration.typeConverter)
        val recognizedActionParameters = convertArgument.getParametersForMethod(methodInfo,
                parsedArguments.RecognizedArgumentsAsPairs(),
                _typeContainer)
        val m = ParsedMethod(parsedArguments,
                _controller.type,
                methodInfo,recognizedActionParameters.toTypedArray(),
                _typeContainer, _configuration)
        return m
    }

}
