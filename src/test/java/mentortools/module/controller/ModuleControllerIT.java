package mentortools.module.controller;

import mentortools.module.model.Module;
import mentortools.module.model.dto.AddModuleCommand;
import mentortools.module.model.dto.CreateModuleCommand;
import mentortools.module.model.dto.ModuleDto;
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
class ModuleControllerIT {

    @Autowired
    TestRestTemplate template;
    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    SyllabusRepository syllabusRepository;
    @Autowired
    ModelMapper modelMapper;

    private Syllabus syllabus;
    private Module module1;
    private Module module2;

    @BeforeEach
    void init() {
        syllabus = new Syllabus("JPA");
        module1 = new Module("Bevezeto", "jpa/bevezeto");
        module2 = new Module("Befejezo", "jpa/befejezo");
    }

    @Test
    void getModuleById() {
        moduleRepository.save(module1);

        ModuleDto result = template.getForObject("/api/modules/{id}",
                ModuleDto.class, module1.getId());

        assertNotNull(result);
        assertEquals(module1.getTitle(), result.getTitle());
    }

    @Test
    void getModuleWithWrongId() {
        moduleRepository.save(module1);

        Problem problem = template.exchange("/api/modules/{id}",
                HttpMethod.GET,
                null,
                Problem.class,
                10 * module1.getId()).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void listModules() {
        moduleRepository.save(module1);
        moduleRepository.save(module2);

        List<ModuleDto> result = template.exchange("/api/modules",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ModuleDto>>() {
                },
                module1.getId()).getBody();

        assertThat(result)
                .hasSize(2)
                .extracting("title")
                .containsExactly(module1.getTitle(), module2.getTitle());

    }

    @Test
    void createModule() {
        ModuleDto result = template.postForObject("/api/modules",
                new CreateModuleCommand(module1.getTitle(), module1.getURL()),
                ModuleDto.class);

        assertNotNull(result);
        assertEquals(module1.getTitle(), result.getTitle());
    }

    @Test
    void updateModule() {
        moduleRepository.save(module1);

        ModuleDto result = template.exchange("/api/modules/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(new CreateModuleCommand("Valami", "/jpa/valami")),
                ModuleDto.class,
                module1.getId()).getBody();

        assertNotNull(result);
        assertEquals("Valami", result.getTitle());
    }

    @Test
    void updateModuleWithWrongId() {
        moduleRepository.save(module1);

        Problem problem = template.exchange("/api/modules/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(new CreateModuleCommand("Valami", "/jpa/valami")),
                Problem.class,
                10 * module1.getId()).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void deleteModule() {
        moduleRepository.save(module1);

        template.delete("/api/modules/{id}", module1.getId());

        assertFalse(moduleRepository.existsById(module1.getId()));
    }

    @Test
    void deleteModuleWithWrongId() {
        moduleRepository.save(module1);

        Problem problem = template.exchange("/api/modules/{id}",
                HttpMethod.DELETE,
                null,
                Problem.class,
                10 * module1.getId()).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
    }

    @Test
    void addModuleToSyllabus() {
        syllabusRepository.save(syllabus);
        moduleRepository.save(module1);

        SyllabusWithModulesDto result = template.exchange("/api/syllabuses/{id}/modules",
                HttpMethod.PUT,
                new HttpEntity<>(new AddModuleCommand(module1.getId())),
                SyllabusWithModulesDto.class,
                syllabus.getId()).getBody();

        assertNotNull(result);
        assertTrue(result.getModules().contains(modelMapper.map(module1, ModuleDto.class)));
    }

    @Test
    void addModuleWithWrongIdToSyllabus() {
        syllabusRepository.save(syllabus);
        moduleRepository.save(module1);

        Problem problem = template.exchange("/api/syllabuses/{id}/modules",
                HttpMethod.PUT,
                new HttpEntity<>(new AddModuleCommand(10 * module1.getId())),
                Problem.class,
                syllabus.getId()).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
        assertEquals("Module not found", problem.getTitle());
    }

    @Test
    void addModuleToSyllabusWithWrongId() {
        syllabusRepository.save(syllabus);
        moduleRepository.save(module1);

        Problem problem = template.exchange("/api/syllabuses/{id}/modules",
                HttpMethod.PUT,
                new HttpEntity<>(new AddModuleCommand(module1.getId())),
                Problem.class,
                10 * syllabus.getId()).getBody();

        assertNotNull(problem);
        assertEquals(Status.NOT_FOUND, problem.getStatus());
        assertEquals("Syllabus not found", problem.getTitle());
    }

    @Test
    void removeModuleFromSyllabus() {
        moduleRepository.save(module1);
        moduleRepository.save(module2);
        syllabus.addModule(module1);
        syllabus.addModule(module2);
        syllabusRepository.save(syllabus);


        SyllabusWithModulesDto result = template.exchange("/api/syllabuses/{id}/modules",
                HttpMethod.DELETE,
                new HttpEntity<>(new AddModuleCommand(module1.getId())),
                SyllabusWithModulesDto.class,
                syllabus.getId()).getBody();

        assertNotNull(result);
        assertEquals(1, result.getModules().size());
        assertTrue(result.getModules().contains(modelMapper.map(module2, ModuleDto.class)));
    }

    @Test
    void removeNotConatianedModuleFromSyllabus() {
        moduleRepository.save(module1);
        moduleRepository.save(module2);
        syllabus.addModule(module1);
        syllabusRepository.save(syllabus);


        Problem problem = template.exchange("/api/syllabuses/{id}/modules",
                HttpMethod.DELETE,
                new HttpEntity<>(new AddModuleCommand(module2.getId())),
                Problem.class,
                syllabus.getId()).getBody();

        assertNotNull(problem);
        assertEquals(Status.PRECONDITION_FAILED, problem.getStatus());
    }
}