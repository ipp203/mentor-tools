package mentortools.registration.controller;

import mentortools.registration.model.Registration;
import mentortools.registration.model.RegistrationStatus;
import mentortools.registration.model.dto.CreateRegistrationCommand;
import mentortools.registration.model.dto.RegisteredStudentDto;
import mentortools.registration.model.dto.TrainingClassNameDto;
import mentortools.registration.model.dto.UpdateRegistrationCommand;
import mentortools.registration.repository.RegistrationRepository;
import mentortools.student.model.Student;
import mentortools.student.repository.StudentRepository;
import mentortools.trainingclass.model.TrainingClass;
import mentortools.trainingclass.repository.TrainingClassRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/delete_tables.sql")
class RegistrationControllerIT {

    @Autowired
    TestRestTemplate template;

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TrainingClassRepository classRepository;
    @Autowired
    RegistrationRepository registrationRepository;

    Student student1 = new Student("John Doe", "johndoe@doe.hu", "johnydoe", "No comment");
    Student student2 = new Student("Jane Doe", "janedoe@doe.hu", "janydoe", "No comment");
    Student student3 = new Student("Jack Doe", "jackdoe@doe.hu", "jackydoe", "No comment");

    TrainingClass class1 = new TrainingClass("Strukturavaltas", LocalDate.now());
    TrainingClass class2 = new TrainingClass("Strukturavaltas2", LocalDate.now());

    @BeforeEach
    void init() {
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);

        classRepository.save(class1);
        classRepository.save(class2);
    }

    @Test
    void createRegistration() {

        RegisteredStudentDto student = template.postForObject("/api/trainingclasses/{id}/registrations",
                new CreateRegistrationCommand(student1.getId()),
                RegisteredStudentDto.class,
                Map.of("id", class1.getId()));

        assertEquals(RegistrationStatus.ACTIVE, student.getStatus());

        List<Registration> result = registrationRepository.findAll();

        assertThat(result)
                .hasSize(1)
                .extracting("student", "trainingClass")
                .contains(tuple(student1, class1));
    }

    @Test
    void createRegistrationWithWrongStudentId() {
        long wrongStudentId = student1.getId() + student2.getId() + student3.getId();

        Problem problem = template.exchange("/api/trainingclasses/{id}/registrations",
                HttpMethod.POST,
                new HttpEntity<>(new CreateRegistrationCommand(wrongStudentId)),
                Problem.class,
                Map.of("id", class1.getId())).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void createRegistrationWithWrongTrainingClassId() {

        Problem problem = template.exchange("/api/trainingclasses/{id}/registrations",
                HttpMethod.POST,
                new HttpEntity<>(new CreateRegistrationCommand(student1.getId())),
                Problem.class,
                0).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void updateRegistration() {
        registrationRepository.save(new Registration(student1, class1, RegistrationStatus.ACTIVE));

        template.exchange("/api/trainingclasses/{id}/registrations",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateRegistrationCommand(student1.getId(), RegistrationStatus.EXIT_IN_PROGRESS)),
                RegisteredStudentDto.class,
                Map.of("id", class1.getId())).getBody();

        Optional<Registration> result = registrationRepository.findRegistrationByStudentIdAndTrainingClassId(student1.getId(), class1.getId());

        assertTrue(result.isPresent());
        assertEquals(RegistrationStatus.EXIT_IN_PROGRESS, result.get().getRegistrationStatus());
    }

    @Test
    void updateRegistrationWithWrongStudentId() {
        registrationRepository.save(new Registration(student1, class1, RegistrationStatus.ACTIVE));
        long wrongStudentId = student1.getId() + student2.getId() + student3.getId();

        Problem problem = template.exchange("/api/trainingclasses/{id}/registrations",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateRegistrationCommand(wrongStudentId, RegistrationStatus.EXIT_IN_PROGRESS)),
                Problem.class,
                Map.of("id", class1.getId())).getBody();
        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void updateRegistrationWithWrongTrainingClassId() {
        registrationRepository.save(new Registration(student1, class1, RegistrationStatus.ACTIVE));

        Problem problem = template.exchange("/api/trainingclasses/{id}/registrations",
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateRegistrationCommand(student1.getId(), RegistrationStatus.EXIT_IN_PROGRESS)),
                Problem.class,
                Map.of("id", 0)).getBody();
        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void listRegisteredStudents() {
        registrationRepository.save(new Registration(student1, class1, RegistrationStatus.ACTIVE));
        registrationRepository.save(new Registration(student2, class2, RegistrationStatus.ACTIVE));
        registrationRepository.save(new Registration(student3, class2, RegistrationStatus.ACTIVE));

        List<RegisteredStudentDto> result = template.exchange("/api/trainingclasses/{id}/registrations",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RegisteredStudentDto>>(){},
                class2.getId()).getBody();

        assertThat(result)
                .hasSize(2)
                .extracting(RegisteredStudentDto::getName)
                .containsExactly(student2.getName(), student3.getName());
    }

    @Test
    void listTrainingClassesByStudentId() {
        registrationRepository.save(new Registration(student1, class1, RegistrationStatus.ACTIVE));
        registrationRepository.save(new Registration(student1, class2, RegistrationStatus.ACTIVE));
        registrationRepository.save(new Registration(student3, class2, RegistrationStatus.ACTIVE));

        List<TrainingClassNameDto> result = template.exchange("/api/students/{id}/registrations",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TrainingClassNameDto>>(){},
                student1.getId()).getBody();

        assertThat(result)
                .hasSize(2)
                .extracting(TrainingClassNameDto::getName)
                .containsExactly(class1.getName(), class2.getName());

    }
}