package mentortools.syllabus.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mentortools.syllabus.model.dto.AssignSyllabusToTrainingClassCommand;
import mentortools.syllabus.model.dto.CreateSyllabusCommand;
import mentortools.syllabus.model.dto.SyllabusDto;
import mentortools.syllabus.service.SyllabusService;
import mentortools.trainingclass.model.dto.TrainingClassWithSyllabusDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "Syllabus administration")
public class SyllabusController {

    SyllabusService service;

    public SyllabusController(SyllabusService service) {
        this.service = service;
    }

    @GetMapping("/api/syllabuses")
    @Operation(summary = "List syllabuses")
    public List<SyllabusDto> listSyllabuses() {
        return service.listSyllabuses();
    }

    @GetMapping("/api/syllabuses/{id}")
    @Operation(summary = "Get syllabus by id")
    public SyllabusDto getSyllabusById(@PathVariable("id") long id) {
        return service.getSyllabusById(id);
    }

    @PostMapping("/api/syllabuses")
    @Operation(summary = "Create syllabus")
    public SyllabusDto createSyllabus(@Valid @RequestBody CreateSyllabusCommand command) {
        return service.saveSyllabus(command);
    }

    @DeleteMapping("/api/syllabuses/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete syllabus by id")
    public void deleteSyllabus(@PathVariable("id") long id) {
        service.deleteSyllabus(id);
    }

    @PostMapping("/api/trainingclasses/{id}/syllabuses")
    @Operation(summary = "Add syllabus to training class")
    public TrainingClassWithSyllabusDto assignSyllabusToTrainingClass(@PathVariable("id") long trainingId, @Valid @RequestBody AssignSyllabusToTrainingClassCommand command) {
        return service.assignSyllabus(trainingId, command);
    }

    @PutMapping("/api/trainingclasses/{id}/syllabuses")
    @Operation(summary = "Change syllabus in training class by id")
    public TrainingClassWithSyllabusDto updateSyllabus(@PathVariable("id") long trainingId, @Valid @RequestBody AssignSyllabusToTrainingClassCommand command) {
        return service.updateSyllabus(trainingId, command);
    }

    @GetMapping("/api/trainingclasses/{id}/syllabuses")
    @Operation(summary = "Get training class with syllabus")
    public TrainingClassWithSyllabusDto getTrainingClassWithSyllabus(@PathVariable("id") long id){
        return service.getTrainigClassWithSyllabus(id);
    }
}
