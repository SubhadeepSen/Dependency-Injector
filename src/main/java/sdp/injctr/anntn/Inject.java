package sdp.injctr.anntn;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A field level annotation which helps to inject required bean and should be
 * used inside a class annotated with <b>@InjectIn</b>
 * 
 * @author Subhadeep Sen
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inject {

}