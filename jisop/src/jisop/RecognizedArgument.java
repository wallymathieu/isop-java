package jisop;

/**
 *
 * @author mathieu
 */
public class RecognizedArgument {
    /// <summary>
    /// the matched value if any, for instance the "value" of the expression "--argument value"
    /// </summary>

    public final String value;
    public final ArgumentWithOptions withOptions;
    /// <summary>
    /// the "argument" of the expression "--argument"
    /// </summary>
    public final String argument;

    public RecognizedArgument(ArgumentWithOptions argumentWithOptions,
            String parameter) {
        this(argumentWithOptions, parameter, null);
    }

    public RecognizedArgument(ArgumentWithOptions argumentWithOptions,
            String parameter, String value) {
        this.value = value;
        withOptions = argumentWithOptions;
        argument = parameter;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.value);
        hash = 79 * hash + Objects.hashCode(this.withOptions);
        hash = 79 * hash + Objects.hashCode(this.argument);
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
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (!Objects.equals(this.withOptions, other.withOptions)) {
            return false;
        }
        if (!Objects.equals(this.argument, other.argument)) {
            return false;
        }
        return true;
    }
    public String toString(){
        return String.format("%1s: %2s", this.argument,this.value);
    }
}
