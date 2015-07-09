package jisop.domain;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by mathieu.
 */
public class Argument {
    public final String description;
    public final String name;
    public final Consumer<String> action;
    public final boolean required;
    public final Class type;
    public Argument(String name, Consumer<String> action, boolean required, String description ,Class type)
    {
        this.description = description;
        this.name = name;
        this.action = action;
        this.required = required;
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.description);
        hash = 79 * hash + Objects.hashCode(this.name);
        hash = 79 * hash + Objects.hashCode(this.required);
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
        final Argument other = (Argument) obj;
        return Objects.equals(this.description, other.description) &&
                Objects.equals(this.name, other.name) &&
                Objects.equals(this.required, other.required);
    }
    public String toString(){
        return String.format("%s:%s", name, description);
    }
}
