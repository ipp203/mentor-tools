package mentortools.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StringLengthValidator implements ConstraintValidator<ValidStringLength, String> {

    private int length;

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
        return text.length() <= length;
    }

    @Override
    public void initialize(ValidStringLength constraintAnnotation) {
        length = constraintAnnotation.length();
    }
}
