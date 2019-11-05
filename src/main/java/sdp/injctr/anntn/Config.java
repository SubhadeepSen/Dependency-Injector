package sdp.injctr.anntn;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method level annotation to create configuration bean and should be used
 * inside a class annotated with <b> @InjectFrom </b>
 * 
 * @author Subhadeep Sen
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Config {

}