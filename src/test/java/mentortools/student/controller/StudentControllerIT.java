package mentortools.student.controller;

import mentortools.student.model.Student;
import mentortools.student.model.dto.CreateStudentCommand;
import mentortools.student.model.dto.StudentDto;
import mentortools.student.model.dto.UpdateStudentCommand;
import mentortools.student.repository.StudentRepository;
import mentortools.trainingclass.model.TrainingClass;
import mentortools.trainingclass.model.TrainingClassDates;
import mentortools.trainingclass.model.dto.TrainingClassDto;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/delete_tables.sql")
class StudentControllerIT {
    private static final String BASE_URL = "/api/students";

    @Autowired
    TestRestTemplate template;

    @Autowired
    StudentRepository repository;

    @Test
    void createStudent() {
        TrainingClassDto student = template.postForObject(BASE_URL,
                new CreateStudentCommand("Isoczki Pal", "abcd@abcd.hu", null, ""),
                TrainingClassDto.class);


        Optional<Student> result = repository.findById(student.getId());

        assertTrue(result.isPresent());
        assertEquals("Isoczki Pal", result.get().getName());
    }

    @Test
    void listStudents() {
        repository.save(new Student("Isoczki Pal", "abcd@abcd.com", null, ""));
        repository.save(new Student("Isoczki Peter", "dcba@dcba.hu", "peter", "Legokosabb tanulo"));

        List<StudentDto> result = template.exchange(BASE_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StudentDto>>() {
                }).getBody();

        assertThat(result)
                .hasSize(2)
                .extracting(StudentDto::getName)
                .contains("Isoczki Pal", "Isoczki Peter");

        List<StudentDto> filteredResult = template.exchange(BASE_URL+"?name=peter",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StudentDto>>() {
                }).getBody();

        assertThat(filteredResult)
                .hasSize(1)
                .extracting(StudentDto::getName)
                .contains("Isoczki Peter");
    }

    @Test
    void testGetStudent() {
        Student student = repository.save(new Student("Isoczki Pal", "abcd@abcd.com", null, ""));

        StudentDto result = template.getForObject(BASE_URL + "/" + student.getId(), StudentDto.class);

        assertEquals("Isoczki Pal", result.getName());
    }

    @Test
    void testGetStudentWithWrongId() {
        Student student = repository.save(new Student("Isoczki Pal", "abcd@abcd.com", null, ""));

        Problem problem = template.exchange(BASE_URL + "/" + (1000 * student.getId()),
                HttpMethod.GET,
                null,
                Problem.class).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void updateStudent() {
        Student student = repository.save(new Student("Isoczki Pal", "abcd@abcd.com", null, ""));

        template.put(BASE_URL + "/" + student.getId(), new UpdateStudentCommand("Isoczki Peter",
                "dcba@dcba.hu",
                "peter",
                "Legokosabb tanulo"));

        Optional<Student> result = repository.findById(student.getId());

        assertTrue(result.isPresent());
        assertEquals("Isoczki Peter", result.get().getName());
        assertEquals("Legokosabb tanulo", result.get().getComment());
    }

    @Test
    void updateStudentWithWrongEmail() {
        Student student = repository.save(new Student("Isoczki Pal", "abcd@abcd.com", null, ""));

        Problem problem = template.exchange(BASE_URL + "/" + student.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateStudentCommand("Isoczki Peter",
                        "dcba.hu",
                        "peter",
                        "Legokosabb tanulo")),
                Problem.class).getBody();

        assertNotNull(problem);
        assertEquals(Status.BAD_REQUEST, problem.getStatus());
    }

    @Test
    void deleteStudent() {
        Student student = repository.save(new Student("Isoczki Pal", "abcd@abcd.com", null, ""));

        template.delete(BASE_URL + "/" + student.getId());

        List<Student> result = repository.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());

    }

}