package mentortools.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = StringLengthValidator.class)
public @interface ValidStringLength {
    String message() default "Too long name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int length() default 255;
}
