package mentortools.student.controller;

import io.swagger.v3.oas.annotations.Operation;
import mentortools.student.model.dto.CreateStudentCommand;
import mentortools.student.model.dto.StudentDto;
import mentortools.student.model.dto.UpdateStudentCommand;
import mentortools.student.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "List students.")
    public List<StudentDto> listStudents(@RequestParam Optional<String> name) {
        return service.listStudents(name);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by id.")
    public StudentDto getStudent(@PathVariable("id") long id) {
        return service.getStudentById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create student")
    public StudentDto createStudent(@Valid @RequestBody CreateStudentCommand command) {
        return service.saveStudent(command);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student's data")
    public StudentDto updateStudent(@PathVariable("id") long id, @Valid @RequestBody UpdateStudentCommand command) {
        return service.updateStudent(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete student by id")
    public void deleteStudent(@PathVariable("id") long id) {
        service.deleteStudent(id);
    }

}
