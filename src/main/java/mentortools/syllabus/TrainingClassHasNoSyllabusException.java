package mentortools.syllabus;

import mentortools.trainingclass.model.TrainingClass;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class TrainingClassHasNoSyllabusException extends AbstractThrowableProblem {
    public TrainingClassHasNoSyllabusException(TrainingClass trainingClass) {
        super(URI.create("trainingclass/has-no-syllabus"),
                "Training class has not got syllabus",
                Status.PRECONDITION_FAILED,
                String.format("Training class with id %d and name %s has not got syllabus yet.", trainingClass.getId(), trainingClass.getName()));
    }
}
