package mentortools.module.repository;

import mentortools.EntityNotFoundException;
import mentortools.module.model.Module;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class ModuleRepositoryOperation {

    private ModuleRepository repository;

    public ModuleRepositoryOperation(ModuleRepository repository) {
        this.repository = repository;
    }

    public Module findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        URI.create("modules/not_found"),
                        "Module not found",
                        "Module not found with id: " + id));
    }
}
