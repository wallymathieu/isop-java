package isop.command_line.parse;

import isop.domain.Argument;

import java.util.function.Consumer;

/**
 * class to enable extensions of the behavior of what is recognized as
 * arguments.
 *
 * @author mathieu
 */
public class ArgumentWithOptions extends Argument {
    public final ArgumentParameter argument;
    public ArgumentWithOptions(ArgumentParameter argument,
                               Consumer<String> action,
                               boolean required,
                               String description,
                               Class type) {
        super(argument!=null? argument.longAlias() : null, action, required, description, type);
        this.argument = argument;
    }
    public ArgumentWithOptions(ArgumentParameter argument) {
        this(argument, null, false, null, null);
    }
    public ArgumentWithOptions required(boolean required){
        return new ArgumentWithOptions(this.argument, this.action, required, description, type);
    }

    public ArgumentWithOptions type(Class type){
        return new ArgumentWithOptions(this.argument, this.action, required, description, type);
    }

}
