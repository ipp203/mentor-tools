package mentortools.module.model;

import mentortools.lesson.model.Lesson;
import mentortools.module.model.Module;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class ModuleNotContainsLesson extends AbstractThrowableProblem {

    public ModuleNotContainsLesson(Module module, Lesson lesson){
        super(URI.create("modules/not-contains-lesson"),
                "Module does not contain lesson",
                Status.PRECONDITION_FAILED,
                String.format("Module with id %d and title %s does not contain lesson with id %d and title %s.",
                        module.getId(), module.getTitle(),
                        lesson.getId(), lesson.getTitle()));
    }

}
