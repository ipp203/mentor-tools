package mentortools.lessoncompletion.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mentortools.lessoncompletion.model.dto.CreateLessonCompletionCommand;
import mentortools.lessoncompletion.model.dto.LessonCompletionDto;
import mentortools.lessoncompletion.model.dto.UpdateLessonCompletionCommand;
import mentortools.lessoncompletion.service.LessonCompletionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "Lesson completion administration")
public class LessonCompletionController {
    private final LessonCompletionService service;

    public LessonCompletionController(LessonCompletionService service) {
        this.service = service;
    }

    @PostMapping("/api/students/{id}/lessoncompletions")
    @Operation(summary = "Create lesson completion")
    public LessonCompletionDto createLessonCompletion(@PathVariable("id") long studentId, @Valid @RequestBody CreateLessonCompletionCommand command){
        return service.createLessonCompletion(studentId, command);
    }

    @GetMapping("/api/students/{id}/lessoncompletions")
    @Operation(summary = "List lesson completions by student id")
    public List<LessonCompletionDto> listLessonCompletionsByStudentId(@PathVariable("id")long studentId){
        return service.listLessonCompletionsByStudentId(studentId);
    }

    @GetMapping("/api/lessons/{id}/lessoncompletions")
    @Operation(summary = "List lesson completions by lesson id")
    public List<LessonCompletionDto> listLessonCompletionsByLessonId(@PathVariable("id")long lessonId){
        return service.listLessonCompletionsByLessonId(lessonId);
    }

    @PutMapping("/api/lessoncompletions/{id}")
    @Operation(summary = "Update lesson completion by id")
    public LessonCompletionDto updateLessonCompletion(@PathVariable("id")long lcId, @Valid @RequestBody UpdateLessonCompletionCommand command){
        return service.updateLessonCompletion(lcId, command);
    }

    @DeleteMapping("/api/lessoncompletions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete lesson completion by id")
    public void deleteLessonCompletion(@PathVariable("id")long lcId){
        service.deleteLC(lcId);
    }
}
