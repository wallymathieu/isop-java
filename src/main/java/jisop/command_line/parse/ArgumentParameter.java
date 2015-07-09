package jisop.command_line.parse;

import jisop.infrastructure.Strings;

import java.util.Arrays;

/**
 *
 * @author mathieu
 */
public class ArgumentParameter {
    public ArgumentParameter(String prototype, String[] names, String delimiter, Integer ordinal) {
        this.prototype = prototype;
        aliases = names;
        this.delimiter = delimiter;
        this.ordinal = ordinal;
    }
    public final String prototype;
    public final Integer ordinal;

    public static ArgumentParameter parse(String value) {
        ArgumentParameter ordinalParameter= OrdinalParameter.tryParse(value);
        if (ordinalParameter!=null)
            return ordinalParameter;
        ArgumentParameter optionParameter= OptionParameter.tryParse(value);
        if (optionParameter!=null)
            return optionParameter;
        ArgumentParameter visualStudioParameter= VisualStudioParameter.tryParse(value);
        if (visualStudioParameter!=null)
            return visualStudioParameter;
        throw new RuntimeException("failed to parse "+value);
    }
    public final String[] aliases;
    public final String delimiter;

    public String help() {
        return "--" + Strings.join(", or ", aliases)
                + (Strings.isNullOrEmpty(delimiter)
                ? ""
                : " " + delimiter);
    }

    @Override
    public String toString() {
        return prototype;
    }

    public boolean hasAlias(String value) {
        return Strings.containsStringIgnoreCase(aliases, value);
    }

    public boolean accept(int index, String val) {
        if (ordinal ==null) {
            return hasAlias(val);
        } else {
            return ordinal.equals(index) && hasAlias(val);
        }
    }
    public boolean accept(String val) {
        return hasAlias(val);
    }

    public String longAlias() {
        return Arrays.asList(aliases)
                .stream()
                .max( (a,b) -> Integer.compare(a.length(),b.length()))
                .orElse(null);
    }
}
