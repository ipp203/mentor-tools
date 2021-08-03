package mentortools;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends AbstractThrowableProblem {
    public EntityNotFoundException(URI uri, String title, String detail) {
        super(uri, title, Status.NOT_FOUND, detail);
    }
}
