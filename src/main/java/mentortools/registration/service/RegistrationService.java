package mentortools.registration.service;

import mentortools.EntityNotFoundException;
import mentortools.registration.model.Registration;
import mentortools.registration.model.Status;
import mentortools.registration.model.dto.CreateRegistrationCommand;
import mentortools.registration.model.dto.RegisteredStudentDto;
import mentortools.registration.model.dto.UpdateRegistrationCommand;
import mentortools.registration.repository.RegistrationRepository;
import mentortools.student.model.Student;
import mentortools.student.repository.StudentRepository;
import mentortools.trainingclass.model.TrainingClass;
import mentortools.trainingclass.repository.TrainingClassRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URI;


@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final StudentRepository studentRepository;
    private final TrainingClassRepository trainingClassRepository;

    public RegistrationService(RegistrationRepository registrationRepository, StudentRepository studentRepository, TrainingClassRepository trainingClassRepository) {
        this.registrationRepository = registrationRepository;
        this.studentRepository = studentRepository;
        this.trainingClassRepository = trainingClassRepository;
    }

    @Transactional
    public RegisteredStudentDto createRegistration(long trainingId, CreateRegistrationCommand command) {
        Student student = findStudentById(command.getStudentId());

        TrainingClass trainingClass = findTrainingClassById(trainingId);

        Registration registration = registrationRepository.save(new Registration(student, trainingClass, Status.ACTIVE));
        return new RegisteredStudentDto(student.getId(), student.getName(), registration.getStatus());
    }

    @Transactional
    public RegisteredStudentDto updateRegistration(long trainingId, UpdateRegistrationCommand command) {
        Student student = findStudentById(command.getStudentId());
        TrainingClass trainingClass = findTrainingClassById(trainingId);

        Registration registration = findRegistrationByStudentIdAndTrainingId(student.getId(), trainingClass.getId());
        registration.setStatus(command.getStatus());

        return new RegisteredStudentDto(student.getId(), student.getName(), registration.getStatus());
    }

    private Student findStudentById(long id){
        return studentRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(URI.create("students/student-not-found"),
                        "Student not found",
                        "Student not found with id: " + id));
    }

    private TrainingClass findTrainingClassById(long id){
        return trainingClassRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(URI.create("trainingclasses/training-not-found"),
                        "Training not found",
                        "Training not found with id: " + id));
    }

    private Registration findRegistrationByStudentIdAndTrainingId(long studentId, long trainingId){
        return registrationRepository.findRegistrationByStudentIdAndTrainingClassId(studentId, trainingId)
                .orElseThrow(()->new EntityNotFoundException(URI.create("registrations/registration-not-found"),
                        "Registration not found",
                        String.format("Registration not found with %d student id and %d trtainingclass id", studentId, trainingId)));
    }
}
