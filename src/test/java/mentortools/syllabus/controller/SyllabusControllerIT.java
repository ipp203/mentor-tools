package mentortools.syllabus.controller;

import mentortools.syllabus.model.Syllabus;
import mentortools.syllabus.model.dto.AssignSyllabusToTrainingClassCommand;
import mentortools.syllabus.model.dto.CreateSyllabusCommand;
import mentortools.syllabus.model.dto.SyllabusDto;
import mentortools.syllabus.repository.SyllabusRepository;
import mentortools.trainingclass.model.TrainingClass;
import mentortools.trainingclass.model.dto.TrainingClassWithSyllabusDto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/delete_tables.sql")
class SyllabusControllerIT {

    @Autowired
    TestRestTemplate template;

    @Autowired
    SyllabusRepository syllabusRepository;
    @Autowired
    TrainingClassRepository classRepository;

    TrainingClass class1 = new TrainingClass("Strukturavaltas", LocalDate.now());
    TrainingClass class2 = new TrainingClass("Strukturavaltas2", LocalDate.now());

    Syllabus syllabus1 = new Syllabus("JPA");
    Syllabus syllabus2 = new Syllabus("Spring Data JPA");
    Syllabus syllabus3 = new Syllabus("JUnit");
    Syllabus syllabus4 = new Syllabus("AssertJ");

    @BeforeEach
    void init() {
        classRepository.save(class1);
        classRepository.save(class2);

        syllabusRepository.save(syllabus1);
        syllabusRepository.save(syllabus2);
        syllabusRepository.save(syllabus3);
        syllabusRepository.save(syllabus4);
    }

    @Test
    void listSyllabuses() {
        List<SyllabusDto> result = template.exchange("/api/syllabuses",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SyllabusDto>>() {
                }).getBody();

        assertThat(result)
                .hasSize(4)
                .extracting(SyllabusDto::getName)
                .containsExactly(syllabus1.getName(), syllabus2.getName(), syllabus3.getName(), syllabus4.getName());
    }

    @Test
    void getSyllabusById() {
        syllabus1.addTrainingClass(class1);
        SyllabusDto result = template.getForObject("/api/syllabuses/{id}",
                SyllabusDto.class,
                syllabus1.getId());

        assertEquals(syllabus1.getName(), result.getName());
        assertTrue(syllabus1.getTrainingClasses().contains(class1));
    }

    @Test
    void getSyllabusWithWrongId() {
        long wrongId = syllabus1.getId() + syllabus2.getId() + syllabus3.getId() + syllabus4.getId();
        Problem problem = template.exchange("/api/syllabuses/{id}",
                HttpMethod.GET,
                null,
                Problem.class,
                wrongId).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void createSyllabus() {
        SyllabusDto result = template.postForObject("/api/syllabuses",
                new CreateSyllabusCommand("JPA"),
                SyllabusDto.class);

        assertNotNull(result);
        assertEquals("JPA", result.getName());
    }

    @Test
    void deleteSyllabus() {
        class1.setSyllabus(syllabus1);
        classRepository.save(class1);
        template.delete("/api/syllabuses/{id}", syllabus1.getId());

        assertFalse(syllabusRepository.existsById(syllabus1.getId()));

        template.delete("/api/syllabuses/{id}", syllabus2.getId());

        assertFalse(syllabusRepository.existsById(syllabus2.getId()));
    }

    @Test
    void assignSyllabusToTrainingClass() {
        TrainingClassWithSyllabusDto result = template.postForObject("/api/trainingclasses/{id}/syllabuses",
                new AssignSyllabusToTrainingClassCommand(syllabus1.getId()),
                TrainingClassWithSyllabusDto.class,
                class1.getId());

        assertNotNull(result);
        assertEquals(syllabus1.getName(), result.getSyllabus().getName());
    }


    @Test
    void assignSyllabusToTrainingClassAlreadyHasSyllabus() {
        class1.setSyllabus(syllabus1);
        classRepository.save(class1);
        Problem problem = template.exchange("/api/trainingclasses/{id}/syllabuses",
                HttpMethod.POST,
                new HttpEntity<>(new AssignSyllabusToTrainingClassCommand(syllabus1.getId())),
                Problem.class,
                class1.getId()).getBody();

        assertNotNull(problem);
        assertEquals(Status.PRECONDITION_FAILED, problem.getStatus());
    }


    @Test
    void updateSyllabus() {
        class1.setSyllabus(syllabus1);
        classRepository.save(class1);
        TrainingClassWithSyllabusDto result = template.exchange("/api/trainingclasses/{id}/syllabuses",
                HttpMethod.PUT,
                new HttpEntity<>(new AssignSyllabusToTrainingClassCommand(syllabus2.getId())),
                TrainingClassWithSyllabusDto.class,
                class1.getId()).getBody();

        assertNotNull(result);
        assertEquals(syllabus2.getName(), result.getSyllabus().getName());
    }

    @Test
    void updateSyllabusToTrainingClassHasNoSyllabus() {
        Problem problem = template.exchange("/api/trainingclasses/{id}/syllabuses",
                HttpMethod.PUT,
                new HttpEntity<>(new AssignSyllabusToTrainingClassCommand(syllabus1.getId())),
                Problem.class,
                class1.getId()).getBody();

        assertNotNull(problem);
        assertEquals(Status.PRECONDITION_FAILED, problem.getStatus());
    }

    @Test
    void getTrainingClassWithSyllabus(){
        class1.setSyllabus(syllabus1);
        classRepository.save(class1);

        TrainingClassWithSyllabusDto result = template.getForObject("/api/trainingclasses/{id}/syllabuses",
                TrainingClassWithSyllabusDto.class,
                class1.getId());

        assertNotNull(result);
        assertEquals(syllabus1.getName(), result.getSyllabus().getName());
    }
}