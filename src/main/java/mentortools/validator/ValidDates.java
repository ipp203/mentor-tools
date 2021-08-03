package mentortools.validator;



import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateValidator.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidDates {

    String message() default "The end date must be after start date!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
