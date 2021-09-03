package mentortools.student.service;

import mentortools.student.model.Student;
import mentortools.student.model.dto.CreateStudentCommand;
import mentortools.student.model.dto.StudentDto;
import mentortools.student.model.dto.UpdateStudentCommand;
import mentortools.student.repository.StudentRepository;
import mentortools.student.repository.StudentRepositoryOperation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository repository;
    private final ModelMapper modelMapper;
    private final StudentRepositoryOperation studentRepositoryOperation;

    public StudentService(StudentRepository repository, ModelMapper modelMapper, StudentRepositoryOperation studentRepositoryOperation) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.studentRepositoryOperation = studentRepositoryOperation;
    }

    public List<StudentDto> listStudents(Optional<String> name) {
        return repository.findAll().stream()
                .filter(s->name.isEmpty() || s.getName().toLowerCase().contains(name.get().toLowerCase()))
                .map(c -> modelMapper.map(c, StudentDto.class))
                .collect(Collectors.toList());
    }

    public StudentDto getStudentById(long id) {
        Student student = studentRepositoryOperation.findStudentById(id);
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
        Student student = studentRepositoryOperation.findStudentById(id);

        student.setName(command.getName());
        student.setEmail(command.getEmail());
        student.setGitHubUserName(command.getGitHubUserName());
        student.setComment(command.getComment());

        return modelMapper.map(student, StudentDto.class);
    }

    @Transactional
    public void deleteStudent(long id) {
        repository.delete(studentRepositoryOperation.findStudentById(id));
    }


}
