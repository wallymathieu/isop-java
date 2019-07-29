package isop.command_line.parse

import isop.domain.Argument

import java.util.function.Consumer

/**
 * class to enable extensions of the behavior of what is recognized as
 * arguments.
 *
 * @author mathieu
 */
class ArgumentWithOptions @JvmOverloads constructor(val argument: ArgumentParameter,
                                                    action: Consumer<String>? = null,
                                                    required: Boolean = false,
                                                    description: String? = null) : Argument(argument.longAlias(), action, required, description) {
    fun required(required: Boolean): ArgumentWithOptions {
        return ArgumentWithOptions(this.argument, this.action, required, description)
    }

    fun type(type: Class<*>): ArgumentWithOptions {
        return ArgumentWithOptions(this.argument, this.action, required, description)
    }

}
