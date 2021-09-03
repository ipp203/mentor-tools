package mentortools.trainingclass.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mentortools.trainingclass.model.dto.CreateTrainingClassCommand;
import mentortools.trainingclass.model.dto.TrainingClassDto;
import mentortools.trainingclass.model.dto.UpdateTrainingClassCommand;
import mentortools.trainingclass.service.TrainingClassService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/trainingclasses")
@Tag(name = "Training class administration")
public class TrainingClassController {

    private final TrainingClassService service;

    public TrainingClassController(TrainingClassService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List classes.")
    public List<TrainingClassDto> listClasses() {
        return service.listClasses();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get class by id.")
    public TrainingClassDto getClass(@PathVariable("id") long id) {
        return service.getClassById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create class")
    public TrainingClassDto createClass(@Valid @RequestBody CreateTrainingClassCommand command) {
        return service.saveClass(command);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update class's data")
    public TrainingClassDto updateClass(@PathVariable("id") long id, @Valid @RequestBody UpdateTrainingClassCommand command) {
        return service.updateClass(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete class by id")
    public void deleteClass(@PathVariable("id") long id) {
        service.deleteClass(id);
    }

}
