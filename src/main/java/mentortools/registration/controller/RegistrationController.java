package mentortools.registration.controller;

import mentortools.registration.model.dto.CreateRegistrationCommand;
import mentortools.registration.model.dto.RegisteredStudentDto;
import mentortools.registration.model.dto.UpdateRegistrationCommand;
import mentortools.registration.service.RegistrationService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class RegistrationController {

    private RegistrationService service;

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


}

//    Beiratkozás történhet a /trainingclasses/{id}/registrations címen.
//    Meg kell adni a résztvevő azonosítóját.
//    Itt le lehet kérdezni az évfolyamra beiratkozottakat (a résztvevőkről csak az id-ját, nevét és státuszát adja vissza).
//
//    Egy résztvevő beiratkozásait is le lehet kérdezni a /students/{id}/registrations címen.
//    Csak az évfolyamok id-ját és nevét adja vissza.