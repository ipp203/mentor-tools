package mentortools.syllabus.controller;

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
public class SyllabusController {

    SyllabusService service;

    public SyllabusController(SyllabusService service) {
        this.service = service;
    }

    @GetMapping("/api/syllabuses")
    public List<SyllabusDto> listSyllabuses() {
        return service.listSyllabuses();
    }

    @GetMapping("/api/syllabuses/{id}")
    public SyllabusDto getSyllabusById(@PathVariable("id") long id) {
        return service.getSyllabusById(id);
    }

    @PostMapping("/api/syllabuses")
    public SyllabusDto createSyllabus(@Valid @RequestBody CreateSyllabusCommand command) {
        return service.saveSyllabus(command);
    }

    @DeleteMapping("/api/syllabuses/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteSyllabus(@PathVariable("id") long id) {
        service.deleteSyllabus(id);
    }

    @PostMapping("/api/trainingclasses/{id}/syllabuses")
    public TrainingClassWithSyllabusDto assignSyllabusToTrainingClass(@PathVariable("id") long trainingId, @Valid @RequestBody AssignSyllabusToTrainingClassCommand command) {
        return service.assignSyllabus(trainingId, command);
    }

    @PutMapping("/api/trainingclasses/{id}/syllabuses")
    public TrainingClassWithSyllabusDto updateSyllabus(@PathVariable("id") long trainingId, @Valid @RequestBody AssignSyllabusToTrainingClassCommand command) {
        return service.updateSyllabus(trainingId, command);
    }

    @GetMapping("/api/trainingclasses/{id}/syllabuses")
    public TrainingClassWithSyllabusDto getTrainingClassWithSyllabus(@PathVariable("id") long id){
        return service.getTrainigClassWithSyllabus(id);
    }
}
