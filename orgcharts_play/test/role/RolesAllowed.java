package role;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


/** meta annotation to be applied to annotations that have enum values implementing RoleType. 
 *  the value() method should return an array of objects assignable to RoleType*.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface RolesAllowed { 
    /* deliberately empty */ 
}