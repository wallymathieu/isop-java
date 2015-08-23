package isop.help;

import isop.domain.Argument;

import java.util.Collection;

/**
 * Created by mathieu.
 */
public class HelpForArgumentWithOptions {
    private final Collection<Argument> parameters;

    public HelpForArgumentWithOptions(Collection<Argument> parameters) {

        this.parameters = parameters;
    }
}
