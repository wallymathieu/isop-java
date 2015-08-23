package isop.domain;

import java.util.function.Consumer;

/**
 * Created by mathieu.
 */
public class Field {
    public final String description;
    public final String name;
    public final Consumer<String> action;
    public final boolean required;
    public final Class type;
    public Field(String name,
                 Consumer<String> action,
                 boolean required,
                 String description,
                 Class type)
    {
        this.name = name;
        this.action = action;
        this.required = required;
        this.description = description;
        this.type = type;
    }
}
