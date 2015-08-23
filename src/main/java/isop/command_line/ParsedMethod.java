/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package isop.command_line;

import isop.command_line.parse.ParsedArguments;
import isop.domain.Configuration;
import isop.domain.Method;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 *
 * @author mathieu
 */
public class ParsedMethod extends ParsedArguments {

    private final Function<Class,Object> _typeContainer;
    private final Configuration configuration;
    public Class recognizedClass;
    public Method recognizedAction;
    public Object[] recognizedActionParameters;
    public ParsedMethod(ParsedArguments parsedArguments, Function<Class, Object> _typeContainer, Configuration _configuration) {
        super(parsedArguments);
        this._typeContainer = _typeContainer;

        configuration = _configuration;
    }

    @Override
    public Stream<String> invoke() {
        Object instance = _typeContainer.apply(this.recognizedClass);

        Object retval = null;
        try {
            retval = recognizedAction.invoke(instance, recognizedActionParameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return configuration.formatter.format(retval);
    }
}
