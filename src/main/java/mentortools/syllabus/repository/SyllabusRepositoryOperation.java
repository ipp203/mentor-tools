package mentortools.syllabus.repository;

import mentortools.EntityNotFoundException;
import mentortools.syllabus.model.Syllabus;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class SyllabusRepositoryOperation {
    private final SyllabusRepository repository;

    public SyllabusRepositoryOperation(SyllabusRepository repository) {
        this.repository = repository;
    }

    public Syllabus findSyllabusById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        URI.create("syllabuses/syllabus-not-found"),
                        "Syllabus not found",
                        String.format("Syllabus with id %s not found", id)));
    }
}

