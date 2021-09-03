package mentortools.lesson.repository;

import mentortools.EntityNotFoundException;
import mentortools.lesson.model.Lesson;
import mentortools.lesson.repository.LessonRepository;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class LessonRepositoryOperation {
    private final LessonRepository repository;

    public LessonRepositoryOperation(LessonRepository repository) {
        this.repository = repository;
    }

    public Lesson findLessonById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        URI.create("lessons/not-found"),
                        "Lesson not found",
                        String.format("Lesson with id %d not found", id)));
    }
}
