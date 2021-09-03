package mentortools.lessoncompletion.controller;

import mentortools.lesson.model.Lesson;
import mentortools.lesson.model.dto.LessonDto;
import mentortools.lesson.repository.LessonRepository;
import mentortools.lessoncompletion.model.LessonCompletion;
import mentortools.lessoncompletion.model.PerformStatus;
import mentortools.lessoncompletion.model.TaskCompletion;
import mentortools.lessoncompletion.model.VideoCompletion;
import mentortools.lessoncompletion.model.dto.CreateLessonCompletionCommand;
import mentortools.lessoncompletion.model.dto.LessonCompletionDto;
import mentortools.lessoncompletion.model.dto.UpdateLessonCompletionCommand;
import mentortools.lessoncompletion.repository.LessonCompletionRepository;
import mentortools.student.model.Student;
import mentortools.student.model.dto.StudentDto;
import mentortools.student.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.jdbc.Sql;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/delete_tables.sql")
class LessonCompletionControllerIT {

    @Autowired
    TestRestTemplate template;
    @Autowired
    LessonCompletionRepository lessonCompletionRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    ModelMapper modelMapper;

    Student student1;
    Student student2;

    Lesson lesson1;
    Lesson lesson2;

    @BeforeEach
    void init() {
        student1 = new Student("John Doe", "johndoe@doe.com", "johndoe", "very clever student");
        student2 = new Student("Jane Doe", "janedoe@doe.com", "janedoe", "very stupid student");

        lesson1 = new Lesson("JPA", "/api/lessons/jpa");
        lesson2 = new Lesson("JPQL", "/api/lessons/jpql");

        studentRepository.save(student1);
        studentRepository.save(student2);

        lessonRepository.save(lesson1);
        lessonRepository.save(lesson2);
    }


    @Test
    void createLessonCompletion() {
        LessonCompletionDto result = template.postForObject("/api/students/{id}/lessoncompletions",
                new CreateLessonCompletionCommand(PerformStatus.COMPLETED,
                        PerformStatus.COMPLETED,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "github.com/abcd/123456789",
                        lesson1.getId()),
                LessonCompletionDto.class,
                student1.getId());

        assertEquals(lesson1.getTitle(), result.getLesson().getTitle());
        assertThat(lessonCompletionRepository.findAll())
                .hasSize(1)
                .extracting(LessonCompletion::getStudent)
                .containsExactly(student1);
    }

    @Test
    void createLessonCompletionWithNotExistLessonId() {
        Problem problem = template.exchange("/api/students/{id}/lessoncompletions",
                HttpMethod.POST,
                new HttpEntity<>(new CreateLessonCompletionCommand(PerformStatus.COMPLETED,
                        PerformStatus.COMPLETED,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "github.com/abcd/123456789",
                        lesson1.getId() + lesson2.getId())),
                Problem.class,
                student1.getId()).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());

    }

    @Test
    void listLessonCompletionsByStudentId() {
        saveLessonCompletions();

        List<LessonCompletionDto> result = template.exchange("/api/students/{id}/lessoncompletions",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<LessonCompletionDto>>() {
                },
                student1.getId()).getBody();

        assertThat(result)
                .hasSize(2)
                .extracting(LessonCompletionDto::getLesson)
                .containsExactly(modelMapper.map(lesson1, LessonDto.class), modelMapper.map(lesson2, LessonDto.class));
    }

    @Test
    void listLessonCompletionsByLessonId() {
        saveLessonCompletions();

        List<LessonCompletionDto> result = template.exchange("/api/lessons/{id}/lessoncompletions",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<LessonCompletionDto>>() {
                },
                lesson1.getId()).getBody();

        assertThat(result)
                .hasSize(2)
                .extracting(LessonCompletionDto::getStudent)
                .containsExactly(modelMapper.map(student1, StudentDto.class), modelMapper.map(student2, StudentDto.class));
    }

    @Test
    void updateLessonCompletion() {
        List<LessonCompletion> lessonCompletions = saveLessonCompletions();
        template.put("/api/lessoncompletions/{id}",
                new UpdateLessonCompletionCommand(PerformStatus.NOT_COMPLETED,
                        PerformStatus.NOT_COMPLETED,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        "aaaa",
                        lesson1.getId()),
                lessonCompletions.get(0).getId());

        LessonCompletion result = lessonCompletionRepository.findById(lessonCompletions.get(0).getId()).get();

        assertEquals(PerformStatus.NOT_COMPLETED, result.getTaskCompletion().getTaskStatus());
        assertEquals(PerformStatus.NOT_COMPLETED, result.getVideoCompletion().getVideoStatus());
    }

    @Test
    void deleteLessonCompletion() {
        List<LessonCompletion> lessonCompletions = saveLessonCompletions();
        template.delete("/api/lessoncompletions/{id}",
                lessonCompletions.get(0).getId());

        assertThat(lessonCompletionRepository.findAll())
                .hasSize(2);
    }

    private List<LessonCompletion> saveLessonCompletions() {
        return List.of(
                lessonCompletionRepository.save(new LessonCompletion(
                        new VideoCompletion(PerformStatus.COMPLETED, LocalDateTime.now()),
                        new TaskCompletion(PerformStatus.COMPLETED, LocalDateTime.now(), "github.com/abcd/1234"),
                        lesson1,
                        student1)),
                lessonCompletionRepository.save(new LessonCompletion(
                        new VideoCompletion(PerformStatus.COMPLETED, LocalDateTime.now()),
                        new TaskCompletion(PerformStatus.COMPLETED, LocalDateTime.now(), "github.com/abcd/5678"),
                        lesson2,
                        student1)),
                lessonCompletionRepository.save(new LessonCompletion(
                        new VideoCompletion(PerformStatus.COMPLETED, LocalDateTime.now()),
                        new TaskCompletion(PerformStatus.COMPLETED, LocalDateTime.now(), "github.com/efgh/3456"),
                        lesson1,
                        student2)));
    }
}