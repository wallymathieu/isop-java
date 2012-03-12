/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop;

import java.util.Objects;

/**
 *
 * @author mathieu
 */
public class RecognizedArgument {
    /// <summary>
    /// the matched value if any, for instance the "value" of the expression "--argument value"
    /// </summary>

    public final String Value;
    public final ArgumentWithOptions WithOptions;
    /// <summary>
    /// the "argument" of the expression "--argument"
    /// </summary>
    public final String Argument;

    public RecognizedArgument(ArgumentWithOptions argumentWithOptions,
            String parameter) {
        this(argumentWithOptions, parameter, null);
    }

    public RecognizedArgument(ArgumentWithOptions argumentWithOptions,
            String parameter, String value) {
        Value = value;
        WithOptions = argumentWithOptions;
        Argument = parameter;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.Value);
        hash = 79 * hash + Objects.hashCode(this.WithOptions);
        hash = 79 * hash + Objects.hashCode(this.Argument);
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
        final RecognizedArgument other = (RecognizedArgument) obj;
        if (!Objects.equals(this.Value, other.Value)) {
            return false;
        }
        if (!Objects.equals(this.WithOptions, other.WithOptions)) {
            return false;
        }
        if (!Objects.equals(this.Argument, other.Argument)) {
            return false;
        }
        return true;
    }
}
