package mentortools.syllabus.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import mentortools.module.model.Module;

import java.net.URI;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class SyllabusNotContainsModuleException extends AbstractThrowableProblem {
    public SyllabusNotContainsModuleException(Syllabus syllabus, Module module){
        super(URI.create("syllabus/has-no-module"),
                "Syllabus has not got module",
                Status.PRECONDITION_FAILED,
                String.format("Syllabus with id %d and name %s has not got module with id %d and title %s.",
                        syllabus.getId(), syllabus.getName(),
                        module.getId(), module.getTitle()));
    }
}
