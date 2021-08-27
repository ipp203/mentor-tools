package mentortools.syllabus;

import mentortools.trainingclass.model.TrainingClass;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class TrainingClassAlreadyHasSyllabusException extends AbstractThrowableProblem {
    public TrainingClassAlreadyHasSyllabusException(TrainingClass trainingClass) {
        super(URI.create("trainingclass/already-has-syllabus"),
                "Training class already has syllabus",
                Status.PRECONDITION_FAILED,
                String.format("Training class with id %d and name %s already has syllabus yet.", trainingClass.getId(), trainingClass.getName()));
    }
}
