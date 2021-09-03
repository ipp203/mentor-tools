package mentortools.lesson.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mentortools.lesson.model.dto.AddLessonCommand;
import mentortools.lesson.model.dto.CreateLessonCommand;
import mentortools.lesson.model.dto.LessonDto;
import mentortools.lesson.service.LessonService;
import mentortools.module.model.dto.ModuleWithLessonsDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "Lesson administration")
public class LessonController {

    private final LessonService service;

    public LessonController(LessonService service) {
        this.service = service;
    }

    @GetMapping("/api/lessons/{id}")
    @Operation(summary = "Get lesson by id")
    public LessonDto getLessonById(@PathVariable("id")long id){
        return service.getLessonById(id);
    }

    @GetMapping("/api/lessons")
    @Operation(summary = "List lessons")
    public List<LessonDto> listLessons(){
        return service.listLessons();
    }

    @PostMapping("/api/lessons")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create lesson")
    public LessonDto createModule(@Valid @RequestBody CreateLessonCommand command){
        return service.saveLesson(command);
    }

    @PutMapping("/api/lessons/{id}")
    @Operation(summary = "Update lesson by id")
    public LessonDto updateLesson(@PathVariable("id")long id, @Valid @RequestBody CreateLessonCommand command){
        return service.changeLesson(id, command);
    }

    @DeleteMapping("/api/lessons/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete lesson by id")
    public void deleteLesson(@PathVariable("id") long id){
        service.deleteLesson(id);
    }

    @PutMapping("/api/modules/{id}/lessons")
    @Operation(summary = "Add lesson to module by ids")
    public ModuleWithLessonsDto addLessonToModule(@PathVariable("id") long moduleId, @Valid @RequestBody AddLessonCommand command){
        return service.addLessonToModule(moduleId, command);
    }

    @DeleteMapping("/api/modules/{id}/lessons")
    @Operation(summary = "Remove lesson from module with ids")
    public ModuleWithLessonsDto removeLessonFromModule(@PathVariable("id") long moduleId, @Valid @RequestBody AddLessonCommand command){
        return service.removeLessonFromModule(moduleId, command);
    }
}
