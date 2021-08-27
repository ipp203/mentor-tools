package mentortools.trainingclass.repository;

import mentortools.EntityNotFoundException;
import mentortools.trainingclass.model.TrainingClass;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class TrainingClassRepositoryOperation {
    private TrainingClassRepository repository;

    public TrainingClassRepositoryOperation(TrainingClassRepository repository) {
        this.repository = repository;
    }

    public TrainingClass getTrainingClassById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(URI.create("trainingclasses/trainingclass-not-found"),
                        "Training class not found",
                        "Training class not found with id: " + id));
    }
}
