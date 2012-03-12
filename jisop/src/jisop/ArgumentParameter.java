/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jisop;

/**
 *
 * @author mathieu
 */
public class ArgumentParameter {

    public ArgumentParameter(String prototype, String[] names) {
        this(prototype, names, null, null);
    }

    public ArgumentParameter(String prototype, String[] names, String delimiter, Integer ordinal) {
        Prototype = prototype;
        Aliases = names;
        Delimiter = delimiter;
        Ordinal = ordinal;
    }
    public final String Prototype;
    public final Integer Ordinal;

    public static ArgumentParameter Parse(String value) {
        ArgumentParameter ordinalParameter=OrdinalParameter.TryParse(value);
        if (ordinalParameter!=null)
                return ordinalParameter;
        ArgumentParameter optionParameter=OptionParameter.TryParse(value);
        if (optionParameter!=null)
                return optionParameter;
        ArgumentParameter visualStudioParameter=VisualStudioParameter.TryParse(value);
        if (visualStudioParameter!=null)
                return visualStudioParameter;
        throw new RuntimeException("failed to parse "+value);
    }
    public final String[] Aliases;
    public final String Delimiter;

    public String help() {
        return "--" + StringUtils.join(", or ", Aliases)
                + (StringUtils.isNullOrEmpty(Delimiter)
                ? ""
                : " " + Delimiter);
    }

    @Override
    public String toString() {
        return Prototype;
    }

    public boolean hasAlias(String value) {
        return StringUtils.containsStringIgnoreCase(Aliases, value);
    }

    public boolean accept(int index, String val) {
        if (Ordinal!=null) {
            return hasAlias(val);
        } else {
            return Ordinal.intValue() == index && hasAlias(val);
        }
    }
}
