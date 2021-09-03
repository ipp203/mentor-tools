package mentortools.lesson.service;

import mentortools.lesson.model.Lesson;
import mentortools.lesson.model.dto.AddLessonCommand;
import mentortools.lesson.model.dto.CreateLessonCommand;
import mentortools.lesson.model.dto.LessonDto;
import mentortools.lesson.repository.LessonRepository;
import mentortools.lesson.repository.LessonRepositoryOperation;
import mentortools.module.model.Module;
import mentortools.module.model.ModuleNotContainsLesson;
import mentortools.module.model.dto.ModuleWithLessonsDto;
import mentortools.module.repository.ModuleRepositoryOperation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private final ModelMapper modelMapper;
    private final ModuleRepositoryOperation moduleRepositoryOperation;
    private final LessonRepositoryOperation lessonRepositoryOperation;

    public LessonService(LessonRepository lessonRepository, ModelMapper modelMapper, ModuleRepositoryOperation moduleRepositoryOperation, LessonRepositoryOperation lessonRepositoryOperation) {
        this.lessonRepository = lessonRepository;
        this.modelMapper = modelMapper;
        this.moduleRepositoryOperation = moduleRepositoryOperation;
        this.lessonRepositoryOperation = lessonRepositoryOperation;
    }

    public LessonDto getLessonById(long id) {
        return modelMapper.map(lessonRepositoryOperation.findLessonById(id), LessonDto.class);
    }

    public List<LessonDto> listLessons() {
        return lessonRepository.findAll().stream()
                .map(l->modelMapper.map(l,LessonDto.class))
                .collect(Collectors.toList());
    }

    public LessonDto saveLesson(CreateLessonCommand command) {
        Lesson lesson = lessonRepository.save(new Lesson(command.getTitle(), command.getUrl()));
        return modelMapper.map(lesson, LessonDto.class);
    }

    public LessonDto changeLesson(long id, CreateLessonCommand command) {
       Lesson lesson = lessonRepositoryOperation.findLessonById(id);
       lesson.setTitle(command.getTitle());
       lesson.setUrl(command.getUrl());
       return modelMapper.map(lesson, LessonDto.class);
    }

    public void deleteLesson(long id) {
        Lesson lesson = lessonRepositoryOperation.findLessonById(id);
        lessonRepository.delete(lesson);
    }

    @Transactional
    public ModuleWithLessonsDto addLessonToModule(long moduleId, AddLessonCommand command) {
        Module module = moduleRepositoryOperation.findById(moduleId);
        Lesson lesson = lessonRepositoryOperation.findLessonById(command.getLessonId());
        module.addLesson(lesson);
        return modelMapper.map(module, ModuleWithLessonsDto.class);
    }

    @Transactional
    public ModuleWithLessonsDto removeLessonFromModule(long moduleId, AddLessonCommand command) {
        Module module = moduleRepositoryOperation.findById(moduleId);
        Lesson lesson = lessonRepositoryOperation.findLessonById(command.getLessonId());
        removeLessonFromModule(module, lesson);
        return modelMapper.map(module, ModuleWithLessonsDto.class);
    }

    public void removeLessonFromModule(Module module, Lesson lesson) {
        if (module.getLessons() == null || !module.getLessons().contains(lesson)) {
            throw new ModuleNotContainsLesson(module, lesson);
        }
        module.getLessons().remove(lesson);
    }
}
