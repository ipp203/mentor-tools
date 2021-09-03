package mentortools.student.repository;

import mentortools.EntityNotFoundException;
import mentortools.student.model.Student;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class StudentRepositoryOperation {
    private final StudentRepository repository;

    public StudentRepositoryOperation(StudentRepository repository) {
        this.repository = repository;
    }

    public Student findStudentById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(URI.create("students/student-not-found"),
                        "Student not found",
                        "Student not found with id: " + id));
    }
}
