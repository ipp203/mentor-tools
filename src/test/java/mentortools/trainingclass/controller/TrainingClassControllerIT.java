package mentortools.trainingclass.controller;

import mentortools.trainingclass.model.dto.CreateTrainingClassCommand;
import mentortools.trainingclass.model.dto.TrainingClassDto;
import mentortools.trainingclass.model.dto.UpdateTrainingClassCommand;
import mentortools.trainingclass.model.TrainingClass;
import mentortools.trainingclass.model.TrainingClassDates;
import mentortools.trainingclass.repository.TrainingClassRepository;
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
class TrainingClassControllerIT {

    private static final String BASE_URL = "/api/trainingclasses";

    @Autowired
    TestRestTemplate template;

    @Autowired
    TrainingClassRepository repository;

    @Test
    void createClass() {
        TrainingClassDto trainingClass = template.postForObject(BASE_URL,
                new CreateTrainingClassCommand("Strukturavalto", LocalDate.of(2020, 10, 28)),
                TrainingClassDto.class);


        Optional<TrainingClass> result = repository.findById(trainingClass.getId());

        assertTrue(result.isPresent());
        assertEquals("Strukturavalto", result.get().getName());
    }

    @Test
    void listClasses() {
        repository.save(new TrainingClass("Strukturavalto", LocalDate.of(2020, 10, 28)));
        repository.save(new TrainingClass("Java-Halado", LocalDate.of(2021, 5, 29)));

        List<TrainingClassDto> result = template.exchange(BASE_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TrainingClassDto>>() {
                }).getBody();


        assertThat(result).hasSize(2).extracting(TrainingClassDto::getName).contains("Strukturavalto", "Java-Halado");
    }

    @Test
    void testGetClass() {
        TrainingClass tc1 = repository.save(new TrainingClass("Strukturavalto", LocalDate.of(2020, 10, 28)));

        TrainingClassDto result = template.getForObject(BASE_URL + "/" + tc1.getId(), TrainingClassDto.class);

        assertEquals("Strukturavalto", result.getName());
    }

    @Test
    void testGetClassWithWrongId() {
        TrainingClass tc1 = repository.save(new TrainingClass("Strukturavalto", LocalDate.of(2020, 10, 28)));

        Problem problem = template.exchange(BASE_URL + "/" + (1000 * tc1.getId()),
                HttpMethod.GET,
                null,
                Problem.class).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void updateClass() {
        TrainingClass tc1 = repository.save(new TrainingClass("Strukturavalto", LocalDate.of(2020, 10, 28)));

        template.put(BASE_URL + "/" + tc1.getId(), new UpdateTrainingClassCommand("Java-Halado",
                new TrainingClassDates(LocalDate.of(2021, 5, 29),
                        LocalDate.of(2021, 8, 9))));

        Optional<TrainingClass> result = repository.findById(tc1.getId());

        assertTrue(result.isPresent());
        assertEquals("Java-Halado", result.get().getName());
        assertEquals(LocalDate.of(2021, 8, 9), result.get().getDates().getEndDate());
    }

    @Test
    void updateClassWithWrongDates() {
        TrainingClass tc1 = repository.save(new TrainingClass("Strukturavalto", LocalDate.of(2020, 10, 28)));

        Problem problem = template.exchange(BASE_URL + "/" + tc1.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(new UpdateTrainingClassCommand("Java-Halado",
                        new TrainingClassDates(LocalDate.of(2021, 5, 29),
                                LocalDate.of(2020, 8, 9)))),
                Problem.class).getBody();

        assertNotNull(problem);
        assertEquals(Status.BAD_REQUEST, problem.getStatus());
    }

    @Test
    void deleteClass() {
        TrainingClass tc1 = repository.save(new TrainingClass("Strukturavalto", LocalDate.of(2020, 10, 28)));

        template.delete(BASE_URL + "/" + tc1.getId());

        List<TrainingClass> result = repository.findAll();

        assertNotNull(result);
        assertEquals(0, result.size());

    }
}