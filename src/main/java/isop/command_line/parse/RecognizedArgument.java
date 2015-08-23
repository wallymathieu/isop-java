package isop.command_line.parse;

import isop.domain.Argument;
import isop.infrastructure.KeyValuePair;
import isop.infrastructure.Objects;

/**
 *
 * @author mathieu
 */
public class RecognizedArgument {
    /// <summary>
    /// the matched value if any, for instance the "value" of the expression "--argument value"
    /// </summary>

    public final String value;
    public final Argument argument;
    /// <summary>
    /// the "argument" of the expression "--argument"
    /// </summary>
    public final String rawArgument;
    public final int index;
    public boolean inferredOrdinal;

    public RecognizedArgument(Argument argumentWithOption,
                              int index,
                              String argument) {
        this(argumentWithOption, index, argument, null);
    }

    public RecognizedArgument(
            Argument argumentWithOption,
            int index,
            String argument,
            String value) {
        this.value = value;
        this.index = index;
        this.argument = argumentWithOption;
        this.rawArgument = argument;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.value);
        hash = 79 * hash + Objects.hashCode(this.index);
        hash = 79 * hash + Objects.hashCode(this.rawArgument);
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
        return Objects.equals(this.value, other.value) &&
               Objects.equals(this.index, other.index) &&
               Objects.equals(this.rawArgument, other.rawArgument) &&
               Objects.equals(this.argument, other.argument);
    }
    public String toString(){
        return String.format("%1s: %2s", this.argument,this.value);
    }

    public KeyValuePair<String, String> asKeyValue() {
        return new KeyValuePair<>(rawArgument,value);
    }
}
