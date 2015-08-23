package isop.test.helpers;

import java.lang.annotation.*;

/**
 * Created by mathieu.
 */
@Target(value={ElementType.METHOD,ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR,ElementType.PARAMETER})
@Retention(value= RetentionPolicy.RUNTIME)
@Documented
public @interface NotNull{
}
