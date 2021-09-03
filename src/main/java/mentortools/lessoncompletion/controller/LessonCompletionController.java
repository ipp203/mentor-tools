package mentortools.lessoncompletion.controller;

import mentortools.lessoncompletion.model.dto.CreateLessonCompletionCommand;
import mentortools.lessoncompletion.model.dto.LessonCompletionDto;
import mentortools.lessoncompletion.model.dto.UpdateLessonCompletionCommand;
import mentortools.lessoncompletion.service.LessonCompletionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class LessonCompletionController {
    private final LessonCompletionService service;

    public LessonCompletionController(LessonCompletionService service) {
        this.service = service;
    }

    @PostMapping("/api/students/{id}/lessoncompletitions")
    public LessonCompletionDto createLessonCompletion(@PathVariable("id") long studentId, @Valid @RequestBody CreateLessonCompletionCommand command){
        return service.createLessonCompletion(studentId, command);
    }

    @GetMapping("/api/students/{id}/lessoncompletitions")
    public List<LessonCompletionDto> listLessonCompletionsByStudentId(@PathVariable("id")long studentId){
        return service.listLessonCompletionsByStudentId(studentId);
    }

    @GetMapping("/api/lessons/{id}/lessoncompletitions")
    public List<LessonCompletionDto> listLessonCompletionsByLessonId(@PathVariable("id")long lessonId){
        return service.listLessonCompletionsByLessonId(lessonId);
    }

    @PutMapping("/api/lessoncompletitions/{id}")
    public LessonCompletionDto updateLessonCompletion(@PathVariable("id")long lcId, @Valid @RequestBody UpdateLessonCompletionCommand command){
        return service.updateLessonCompletion(lcId, command);
    }

    @DeleteMapping("/api/lessoncompletitions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLessonCompletion(@PathVariable("id")long lcId){
        service.deleteLC(lcId);
    }
}
