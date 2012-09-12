package cz.muni.fi.pv243.library.validation;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * It has to be valid ISBN-13 format.
 * 
 * <code>null</code> elements are considered valid
 * 
 * @author <a href="mailto:vramik at redhat.com">Vlastislav Ramik</a>
 */
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = {IsbnValidator.class})
public @interface Isbn {
    String message() default "Isbn has to be valid ISBN-13 format.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default {};

    /**
	 * Defines several <code>@PastYear</code> annotations on the same element
	 */
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		Isbn[] value();
	}
}
