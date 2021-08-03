package mentortools.student.service;

import mentortools.EntityNotFoundException;
import mentortools.student.model.Student;
import mentortools.student.model.dto.CreateStudentCommand;
import mentortools.student.model.dto.StudentDto;
import mentortools.student.model.dto.UpdateStudentCommand;
import mentortools.student.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository repository;
    private final ModelMapper modelMapper;

    public StudentService(StudentRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public List<StudentDto> listStudents() {
        return repository.findAll().stream()
                .map(c -> modelMapper.map(c, StudentDto.class))
                .collect(Collectors.toList());
    }

    public StudentDto getStudentById(long id) {
        Student student = findStudentById(id);
        return modelMapper.map(student, StudentDto.class);
    }

    @Transactional
    public StudentDto saveStudent(CreateStudentCommand command) {
        Student student = new Student(command.getName(), command.getEmail(), command.getGitHubUserName(), command.getComment());
        repository.save(student);
        return modelMapper.map(student, StudentDto.class);
    }

    @Transactional
    public StudentDto updateStudent(long id, UpdateStudentCommand command) {
        Student student = findStudentById(id);

        student.setName(command.getName());
        student.setEmail(command.getEmail());
        student.setGitHubUserName(command.getGitHubUserName());
        student.setComment(command.getComment());

        return modelMapper.map(student, StudentDto.class);
    }

    @Transactional
    public void deleteStudent(long id) {
        repository.delete(findStudentById(id));
    }

    private Student findStudentById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(URI.create("students/student-not-found"),
                        "Student not found",
                        "Student not found with id: " + id));
    }
}
