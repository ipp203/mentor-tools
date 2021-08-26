package mentortools.registration.controller;

import mentortools.registration.model.dto.CreateRegistrationCommand;
import mentortools.registration.model.dto.RegisteredStudentDto;
import mentortools.registration.model.dto.TrainingClassNameDto;
import mentortools.registration.model.dto.UpdateRegistrationCommand;
import mentortools.registration.service.RegistrationService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RegistrationController {

    private final RegistrationService service;

    public RegistrationController(RegistrationService service) {
        this.service = service;
    }

    @PostMapping("/api/trainingclasses/{id}/registrations")
    public RegisteredStudentDto createRegistration(@PathVariable("id") long trainingId, @Valid @RequestBody CreateRegistrationCommand command){
        return service.createRegistration(trainingId, command);
    }

    @PutMapping("/api/trainingclasses/{id}/registrations")
    public RegisteredStudentDto updateRegistration(@PathVariable("id") long trainingId, @Valid @RequestBody UpdateRegistrationCommand command){
        return service.updateRegistration(trainingId, command);
    }

    @GetMapping("/api/trainingclasses/{id}/registrations")
    public List<RegisteredStudentDto> listRegisteredStudents(@PathVariable("id")long trainingId){
        return service.listRegisteredStudents(trainingId);
    }

    @GetMapping("/api/students/{id}/registrations")
    public List<TrainingClassNameDto> listTrainingClassesByStudentId(@PathVariable("id")long studentId){
        return service.listTrainingClassesByStudentId(studentId);
    }
}