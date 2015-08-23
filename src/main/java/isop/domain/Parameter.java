package isop.domain;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by mathieu.
 */
public class Parameter {
    public final Class<?> parameterClass;

    public Parameter(Class<?> aClass) {
        parameterClass = aClass;
    }

    public boolean isClass() {
        return !parameterClass.isPrimitive();
    }
    private static Pattern notNull= Pattern.compile("(not|non)null", Pattern.CASE_INSENSITIVE);
    private class FieldInfoFromField extends FieldInfo {

        private Field f;

        public FieldInfoFromField(Field f) {
            this.f = f;
            this.name = f.getName();
            this.propertyType = f.getType();
            this.looksRequired = Arrays.asList(f.getDeclaredAnnotations())
                    .stream()
                    .anyMatch(a->
                            notNull.matcher( a.annotationType().getSimpleName()
                    ).find());
        }

        @Override
        public void setValue(Object obj, Object value) throws IllegalArgumentException, IllegalAccessException {
            f.set(obj, value);
        }
    }

    private List<FieldInfo> getFields(Class<?> p) {
        List<Field> fields = Arrays.asList(p.getFields());
        return fields.stream()
                .map(FieldInfoFromField::new)
                .collect(Collectors.toList());
    }

    public Collection<FieldInfo> getPublicInstanceProperties(){
        // TODO: Add getters and setters here as well!
        return getFields(parameterClass);
    }
}
