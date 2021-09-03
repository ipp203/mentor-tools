package mentortools.registration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mentortools.registration.model.dto.CreateRegistrationCommand;
import mentortools.registration.model.dto.RegisteredStudentDto;
import mentortools.registration.model.dto.TrainingClassNameDto;
import mentortools.registration.model.dto.UpdateRegistrationCommand;
import mentortools.registration.service.RegistrationService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "Registration administration")
public class RegistrationController {

    private final RegistrationService service;

    public RegistrationController(RegistrationService service) {
        this.service = service;
    }

    @PostMapping("/api/trainingclasses/{id}/registrations")
    @Operation(summary = "Create registration")
    public RegisteredStudentDto createRegistration(@PathVariable("id") long trainingId, @Valid @RequestBody CreateRegistrationCommand command){
        return service.createRegistration(trainingId, command);
    }

    @PutMapping("/api/trainingclasses/{id}/registrations")
    @Operation(summary = "Update registration")
    public RegisteredStudentDto updateRegistration(@PathVariable("id") long trainingId, @Valid @RequestBody UpdateRegistrationCommand command){
        return service.updateRegistration(trainingId, command);
    }

    @GetMapping("/api/trainingclasses/{id}/registrations")
    @Operation(summary = "List registered students")
    public List<RegisteredStudentDto> listRegisteredStudents(@PathVariable("id")long trainingId){
        return service.listRegisteredStudents(trainingId);
    }

    @GetMapping("/api/students/{id}/registrations")
    @Operation(summary = "List training classes by student id")
    public List<TrainingClassNameDto> listTrainingClassesByStudentId(@PathVariable("id")long studentId){
        return service.listTrainingClassesByStudentId(studentId);
    }
}