package mentortools.lesson.controller;

import mentortools.lesson.model.Lesson;
import mentortools.lesson.model.dto.AddLessonCommand;
import mentortools.lesson.model.dto.CreateLessonCommand;
import mentortools.lesson.model.dto.LessonDto;
import mentortools.lesson.repository.LessonRepository;
import mentortools.module.model.Module;
import mentortools.module.model.dto.AddModuleCommand;
import mentortools.module.model.dto.CreateModuleCommand;
import mentortools.module.model.dto.ModuleDto;
import mentortools.module.model.dto.ModuleWithLessonsDto;
import mentortools.module.repository.ModuleRepository;
import mentortools.syllabus.model.Syllabus;
import mentortools.syllabus.model.dto.SyllabusWithModulesDto;
import mentortools.syllabus.repository.SyllabusRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/delete_tables.sql")
class LessonControllerIT {

    @Autowired
    TestRestTemplate template;
    @Autowired
    LessonRepository lessonRepository;
    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    ModelMapper modelMapper;

    private Module module;
    private Lesson lesson1;
    private Lesson lesson2;

    @BeforeEach
    void init() {
        module = new Module("Bevezeto", "jpa/bevezeto");
        lesson1 = new Lesson("JPA mentes", "jpa/bevezeto/mentes");
        lesson2 = new Lesson("JPA muveletek", "jpa/bevezeto/muveletek");
    }

    @Test
    void getLessonById() {
        lessonRepository.save(lesson1);

        LessonDto result = template.getForObject("/api/lessons/{id}",
                LessonDto.class, lesson1.getId());

        assertNotNull(result);
        assertEquals(lesson1.getTitle(), result.getTitle());
    }

    @Test
    void getLessonWithWrongId() {
        lessonRepository.save(lesson1);

        Problem problem = template.exchange("/api/lessons/{id}",
                HttpMethod.GET,
                null,
                Problem.class,
                10 * lesson1.getId()).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void listLessons() {
        lessonRepository.save(lesson1);
        lessonRepository.save(lesson2);

        List<LessonDto> result = template.exchange("/api/lessons",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<LessonDto>>() {},
                lesson1.getId()).getBody();

        assertThat(result)
                .hasSize(2)
                .extracting("title")
                .containsExactly(lesson1.getTitle(), lesson2.getTitle());

    }

    @Test
    void createLesson() {
        LessonDto result = template.postForObject("/api/lessons",
                new CreateLessonCommand(lesson1.getTitle(), lesson1.getURL()),
                LessonDto.class);

        assertNotNull(result);
        assertEquals(lesson1.getTitle(), result.getTitle());
    }

    @Test
    void updateLesson() {
        lessonRepository.save(lesson1);

        LessonDto result = template.exchange("/api/lessons/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(new CreateLessonCommand("Valami", "/jpa/valami")),
                LessonDto.class,
                lesson1.getId()).getBody();

        assertNotNull(result);
        assertEquals("Valami", result.getTitle());
    }

    @Test
    void updateLessonWithWrongId() {
        lessonRepository.save(lesson1);

        Problem problem = template.exchange("/api/lessons/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(new CreateLessonCommand("Valami", "/jpa/valami")),
                Problem.class,
                10 * lesson1.getId()).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void deleteLesson() {
        lessonRepository.save(lesson1);

        template.delete("/api/lessons/{id}", lesson1.getId());

        assertFalse(lessonRepository.existsById(lesson1.getId()));
    }

    @Test
    void deleteLessonWithWrongId() {
        lessonRepository.save(lesson1);

        Problem problem = template.exchange("/api/lessons/{id}",
                HttpMethod.DELETE,
                null,
                Problem.class,
                10 * lesson1.getId()).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void addLessonToModule() {
        moduleRepository.save(module);
        lessonRepository.save(lesson1);

        ModuleWithLessonsDto result = template.exchange("/api/modules/{id}/lessons",
                HttpMethod.PUT,
                new HttpEntity<>(new AddLessonCommand(lesson1.getId())),
                ModuleWithLessonsDto.class,
                module.getId()).getBody();

        assertNotNull(result);
        assertTrue(result.getLessons().contains(modelMapper.map(lesson1, LessonDto.class)));
    }

    @Test
    void addLessonWithWrongIdToModule() {
        moduleRepository.save(module);
        lessonRepository.save(lesson1);

        Problem problem = template.exchange("/api/modules/{id}/lessons",
                HttpMethod.PUT,
                new HttpEntity<>(new AddLessonCommand(10 * lesson1.getId())),
                Problem.class,
                module.getId()).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
        assertEquals("Lesson not found", problem.getTitle());
    }

    @Test
    void addLessonToModuleWithWrongId() {
        moduleRepository.save(module);
        lessonRepository.save(lesson1);

        Problem problem = template.exchange("/api/modules/{id}/lessons",
                HttpMethod.PUT,
                new HttpEntity<>(new AddLessonCommand(lesson1.getId())),
                Problem.class,
                10 * lesson1.getId()).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
        assertEquals("Module not found", problem.getTitle());
    }

    @Test
    void removeLessonFromModule() {
        lessonRepository.save(lesson1);
        lessonRepository.save(lesson2);
        module.addLesson(lesson1);
        module.addLesson(lesson2);
        moduleRepository.save(module);


        ModuleWithLessonsDto result = template.exchange("/api/modules/{id}/lessons",
                HttpMethod.DELETE,
                new HttpEntity<>(new AddLessonCommand(lesson1.getId())),
                ModuleWithLessonsDto.class,
                module.getId()).getBody();

        assertNotNull(result);
        assertEquals(1, result.getLessons().size());
        assertTrue(result.getLessons().contains(modelMapper.map(lesson2, LessonDto.class)));
    }

    @Test
    void removeNotContainedLessonFromModule() {
        lessonRepository.save(lesson1);
        lessonRepository.save(lesson2);
        module.addLesson(lesson1);
        moduleRepository.save(module);


        Problem problem = template.exchange("/api/modules/{id}/lessons",
                HttpMethod.DELETE,
                new HttpEntity<>(new AddLessonCommand(lesson2.getId())),
                Problem.class,
                module.getId()).getBody();

        assertNotNull(problem);
        assertEquals(Status.PRECONDITION_FAILED, problem.getStatus());
    }
}