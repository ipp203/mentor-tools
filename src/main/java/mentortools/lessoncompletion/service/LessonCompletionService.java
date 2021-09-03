package mentortools.lessoncompletion.service;

import mentortools.EntityNotFoundException;
import mentortools.lesson.model.Lesson;
import mentortools.lesson.repository.LessonRepositoryOperation;
import mentortools.lessoncompletion.model.LessonCompletion;
import mentortools.lessoncompletion.model.TaskCompletion;
import mentortools.lessoncompletion.model.VideoCompletion;
import mentortools.lessoncompletion.model.dto.CreateLessonCompletionCommand;
import mentortools.lessoncompletion.model.dto.LessonCompletionDto;
import mentortools.lessoncompletion.model.dto.UpdateLessonCompletionCommand;
import mentortools.lessoncompletion.repository.LessonCompletionRepository;
import mentortools.student.model.Student;
import mentortools.student.repository.StudentRepositoryOperation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonCompletionService {

    private final LessonCompletionRepository lessonCompletionRepository;
    private final StudentRepositoryOperation studentRepositoryOperation;
    private final LessonRepositoryOperation lessonRepositoryOperation;
    private final ModelMapper modelMapper;

    public LessonCompletionService(LessonCompletionRepository lessonCompletionRepository, StudentRepositoryOperation studentRepositoryOperation, LessonRepositoryOperation lessonRepositoryOperation, ModelMapper modelMapper) {
        this.lessonCompletionRepository = lessonCompletionRepository;
        this.studentRepositoryOperation = studentRepositoryOperation;
        this.lessonRepositoryOperation = lessonRepositoryOperation;
        this.modelMapper = modelMapper;
    }

    public LessonCompletionDto createLessonCompletion(long studentId, CreateLessonCompletionCommand command) {
        Student student = studentRepositoryOperation.findStudentById(studentId);
        Lesson lesson = lessonRepositoryOperation.findLessonById(command.getLessonId());
        LessonCompletion lessonCompletion = new LessonCompletion(
                new VideoCompletion(command.getVideoStatus(), command.getTimeOfVideoWatched()),
                new TaskCompletion(command.getTaskStatus(), command.getTimeOfTaskCompleted(), command.getCommitUrl()),
                lesson,
                student);

        lessonCompletionRepository.save(lessonCompletion);
        return modelMapper.map(lessonCompletion, LessonCompletionDto.class);
    }

    public List<LessonCompletionDto> listLessonCompletionsByStudentId(long studentId) {
        Student student = studentRepositoryOperation.findStudentById(studentId);
        return lessonCompletionRepository.findAllByStudentId(student.getId()).stream()
                .map(lc -> modelMapper.map(lc, LessonCompletionDto.class))
                .collect(Collectors.toList());
    }

    public List<LessonCompletionDto> listLessonCompletionsByLessonId(long lessonId) {
        Lesson lesson = lessonRepositoryOperation.findLessonById(lessonId);
        return lessonCompletionRepository.findAllByLessonId(lesson.getId()).stream()
                .map(lc -> modelMapper.map(lc, LessonCompletionDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public LessonCompletionDto updateLessonCompletion(long lcId, UpdateLessonCompletionCommand command) {
        LessonCompletion lessonCompletion = findById(lcId);
        Lesson lesson = lessonRepositoryOperation.findLessonById(command.getLessonId());

        lessonCompletion.setLesson(lesson);
        lessonCompletion.setVideoCompletion(
                new VideoCompletion(command.getVideoStatus(), command.getTimeOfVideoWatched()));
        lessonCompletion.setTaskCompletion(
                new TaskCompletion(command.getTaskStatus(), command.getTimeOfTaskCompleted(), command.getCommitUrl()));

        return modelMapper.map(lessonCompletion, LessonCompletionDto.class);
    }


    public void deleteLC(long lcId) {
        LessonCompletion lessonCompletion = findById(lcId);
        lessonCompletionRepository.delete(lessonCompletion);
    }

    private LessonCompletion findById(long id) {
        return lessonCompletionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        URI.create("lessoncompletions/not-found"),
                        "Lesson completion not found.",
                        String.format("Lesson completion with id %d not found.", id)));
    }
}
