package isop.command_line.parse

import isop.command_line.lex.Token
import isop.command_line.lex.TokenType
import isop.domain.Argument
import isop.infrastructure.ArgumentOutOfRangeException
import isop.infrastructure.Lists
import isop.infrastructure.PeekCollection

import java.util.*
import java.util.stream.Collectors

/**
 *
 * @author mathieu
 */
class ArgumentParser(private val _argumentWithOptions: Collection<Argument>, private val _allowInferParameter: Boolean) {

    fun parse(arg: Map<String, String>): ParsedArguments {
        val recognized = ArrayList<RecognizedArgument>()
        val unRecognizedArguments = ArrayList<UnrecognizedArgument>()
        var index = 0
        for (key in arg.keys) {
            val argumentWithOptions = _argumentWithOptions
                    .stream()
                    .filter { argopt -> accept(argopt, key) }
                    .findFirst()
                    .orElse(null)


            if (null == argumentWithOptions) {
                unRecognizedArguments.add(UnrecognizedArgument(index++, key))
                continue
            }
            recognized.add(RecognizedArgument(
                    argumentWithOptions,
                    index++,
                    key,
                    arg[key]))

        }
        return ParsedArguments(_argumentWithOptions, recognized, unRecognizedArguments)
    }

    fun parse(lexed: List<Token>, arguments: Collection<String>): ParsedArguments {
        val recognizedIndexes = ArrayList<Int>()
        val peekTokens = PeekCollection(lexed)
        var encounteredParameter = false
        val recognized = ArrayList<RecognizedArgument>()
        while (peekTokens.hasMore()) {
            val current = peekTokens.next()
            when (current.tokenType) {
                TokenType.ARGUMENT -> {
                    val argumentWithOptions = _argumentWithOptions
                            .find { argopt -> accept(argopt, current.index, current.value) }

                    if (null == argumentWithOptions && !encounteredParameter && _allowInferParameter) {
                        inferParameter(recognizedIndexes, recognized, current)
                    }else{
                        if (null != argumentWithOptions) {
                            recognizedIndexes.add(current.index)
                            recognized.add(RecognizedArgument(
                                    argumentWithOptions,
                                    current.index,
                                    current.value))
                        }
                    }
                }
                TokenType.PARAMETER -> {
                    encounteredParameter = true
                    val argumentWithOptions = _argumentWithOptions
                            .find { argopt -> accept(argopt, current.index, current.value) }
                    if (argumentWithOptions!=null) {
                        val value: String
                        recognizedIndexes.add(current.index)
                        val next = peekTokens.peek()
                        if (null != next && next.tokenType == TokenType.PARAMETER_VALUE) {
                            val paramValue = peekTokens.next()
                            recognizedIndexes.add(paramValue.index)
                            value = paramValue.value
                        } else {
                            value = ""
                        }

                        recognized.add(RecognizedArgument(
                                argumentWithOptions,
                                current.index,
                                current.value,
                                value))
                    }
                }
                TokenType.PARAMETER_VALUE -> {
                }
                else -> throw ArgumentOutOfRangeException(current.tokenType.toString())
            }
        }

        val argumentList = arguments.toList()

        val unRecognizedArguments = Lists.withIndex(argumentList)
                .filter { tpl -> !recognizedIndexes.contains(tpl.key) }
                .map { tpl -> UnrecognizedArgument(tpl.key, tpl.value) }
                .toList()
        return ParsedArguments(_argumentWithOptions, recognized, unRecognizedArguments)
    }

    private fun inferParameter(recognizedIndexes: MutableList<Int>, recognized: MutableList<RecognizedArgument>, current: Token) {
        val argumentWithOptions: Argument?
        argumentWithOptions = Lists
                .filterWithIndex(_argumentWithOptions) { argOpts, i -> i == current.index }
                .findFirst()
                .orElse(null)
        if (null != argumentWithOptions) {
            recognizedIndexes.add(current.index)
            val arg = RecognizedArgument(
                    argumentWithOptions,
                    current.index,
                    argumentWithOptions.name,
                    current.value)
            arg.inferredOrdinal = true
            recognized.add(arg)
        }
    }

    private fun accept(argument: Argument, index: Int, value: String): Boolean {

        return (argument as? ArgumentWithOptions)?.argument?.accept(index, value)
                ?: ArgumentParameter.parse(argument.name).accept(index, value)
    }

    private fun accept(argument: Argument, value: String): Boolean {

        return (argument as? ArgumentWithOptions)?.argument?.accept(value)
                ?: ArgumentParameter.parse(argument.name).accept(value)
    }

}
