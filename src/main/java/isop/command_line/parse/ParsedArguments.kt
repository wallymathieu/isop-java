package isop.command_line.parse

import isop.MissingArgumentException
import isop.domain.Argument
import isop.infrastructure.KeyValuePair

import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 *
 * @author mathieu
 */
open class ParsedArguments {
    var recognizedArguments: Collection<RecognizedArgument>
    var unRecognizedArguments: Collection<UnrecognizedArgument>
    val argumentWithOptions: Collection<Argument>


    constructor(ArgumentWithOptions: Collection<Argument>,
                recognizedArguments: Collection<RecognizedArgument>,
                UnRecognizedArguments: Collection<UnrecognizedArgument>) {
        this.argumentWithOptions = ArgumentWithOptions
        this.recognizedArguments = recognizedArguments
        this.unRecognizedArguments = UnRecognizedArguments
    }

    constructor(parsedArguments: ParsedArguments) {
        recognizedArguments = parsedArguments.recognizedArguments
        argumentWithOptions = parsedArguments.argumentWithOptions
        unRecognizedArguments = parsedArguments.unRecognizedArguments
    }

    fun UnMatchedRequiredArguments(): Collection<Argument> {
        return argumentWithOptions
                .filter { arg -> arg.required && !isRecognized(arg) }
                .toList()
    }

    open operator fun invoke(): Stream<String> {
        recognizedArguments.stream()
                .filter { arg -> null != arg.argument.action }
                .forEach { arg -> arg.argument.action!!.accept(arg.value!!) }
        return Stream.of("")
    }

    private fun isRecognized(arg: Argument): Boolean {
        return recognizedArguments.stream()
                .anyMatch { a -> a.argument == arg }
    }

    fun merge(parsedMethod: ParsedArguments): ParsedArguments {
        return MergedParsedArguments(this, parsedMethod)
    }

    fun assertFailOnUnMatched() {
        val unMatchedRequiredArguments = UnMatchedRequiredArguments()

        if (unMatchedRequiredArguments.size > 0) {
            throw MissingArgumentException("Missing arguments",
                    unMatchedRequiredArguments.map { a -> a.name }.toTypedArray())
        }
    }

    fun RecognizedArgumentsAsPairs(): Collection<KeyValuePair<String, String?>> {
        return recognizedArguments
                .map { it.asKeyValue() }
                .toList()
    }
}
