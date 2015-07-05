package jisop.command_line.parse;

import jisop.domain.ArgumentAction;
import jisop.infrastructure.Objects;
import jisop.infrastructure.StringUtils;

/**
 * class to enable extensions of the behavior of what is recognized as
 * arguments.
 *
 * @author mathieu
 */
public class ArgumentWithOptions {

    public final String Description;
    public final ArgumentParameter argument;
    public final ArgumentAction action;
    public final boolean Required;

    public ArgumentWithOptions(ArgumentParameter argument,
            boolean required, 
            String description,
            ArgumentAction action) {
        Description = description;
        this.argument = argument;
        Required = required;
        this.action = action;
    }

    public String help() {
        return argument.help()
                + (StringUtils.isNullOrEmpty(Description)
                ? ""
                : "\t" + Description);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.Description);
        hash = 41 * hash + Objects.hashCode(this.argument);
        hash = 41 * hash + (this.Required ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ArgumentWithOptions other = (ArgumentWithOptions) obj;
        if (!Objects.equals(this.Description, other.Description)) {
            return false;
        }
        if (!Objects.equals(this.argument, other.argument)) {
            return false;
        }
        if (this.Required != other.Required) {
            return false;
        }
        return true;
    }
}
