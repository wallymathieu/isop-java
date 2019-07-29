package isop.api

import isop.AppHostBuilder
import isop.abstractions.NotImplementedException
import isop.command_line.ControllerRecognizer
import isop.command_line.parse.ArgumentParser
import isop.command_line.parse.ParsedArguments

/**
 * Created by mathieu.
 */
class ActionControllerExpression(private val controllerName: String, private val actionName: String, private val build: AppHostBuilder) {
    fun parameters(arg: Map<String, String>): ParsedArguments {
        val argumentParser = ArgumentParser(build.globalParameters,
                build._allowInferParameter)
        val parsedArguments = argumentParser.parse(arg)
        val controllerRecognizer = build.controllerRecognizers
                .map { it.value() }
                .find { this.isRecognized(it) }
        if (null != controllerRecognizer) {
            return controllerRecognizer!!.parseArgumentsAndMerge(actionName, arg,
                    parsedArguments)
        }

        parsedArguments.assertFailOnUnMatched()
        return parsedArguments
    }

    private fun isRecognized(recognizer: ControllerRecognizer): Boolean {
        return recognizer.recognize(arrayOf(controllerName, actionName))
    }

    fun help(): String {
        throw NotImplementedException()
    }
}
