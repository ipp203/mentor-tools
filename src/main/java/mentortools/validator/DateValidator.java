package mentortools.validator;

import mentortools.trainingclass.model.TrainingClassDates;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<ValidDates, TrainingClassDates> {

    @Override
    public boolean isValid(TrainingClassDates dates, ConstraintValidatorContext constraintValidatorContext) {
        return dates.getStartDate().isBefore(dates.getEndDate());
    }

    @Override
    public void initialize(ValidDates constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
