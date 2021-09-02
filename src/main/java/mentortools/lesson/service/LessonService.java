package mentortools.lesson.service;

import mentortools.EntityNotFoundException;
import mentortools.lesson.model.Lesson;
import mentortools.lesson.model.dto.AddLessonCommand;
import mentortools.lesson.model.dto.CreateLessonCommand;
import mentortools.lesson.model.dto.LessonDto;
import mentortools.lesson.repository.LessonRepository;
import mentortools.module.model.Module;
import mentortools.module.model.ModuleNotContainsLesson;
import mentortools.module.model.dto.ModuleWithLessonsDto;
import mentortools.module.repository.ModuleRepositoryOperation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private final ModelMapper modelMapper;
    private final ModuleRepositoryOperation moduleRepositoryOperation;

    public LessonService(LessonRepository lessonRepository, ModelMapper modelMapper, ModuleRepositoryOperation moduleRepositoryOperation) {
        this.lessonRepository = lessonRepository;
        this.modelMapper = modelMapper;
        this.moduleRepositoryOperation = moduleRepositoryOperation;
    }

    public LessonDto getLessonById(long id) {
        return modelMapper.map(findById(id), LessonDto.class);
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
       Lesson lesson = findById(id);
       lesson.setTitle(command.getTitle());
       lesson.setUrl(command.getUrl());
       return modelMapper.map(lesson, LessonDto.class);
    }

    public void deleteLesson(long id) {
        Lesson lesson = findById(id);
        lessonRepository.delete(lesson);
    }

    @Transactional
    public ModuleWithLessonsDto addLessonToModule(long moduleId, AddLessonCommand command) {
        Module module = moduleRepositoryOperation.findById(moduleId);
        Lesson lesson = findById(command.getLessonId());
        module.addLesson(lesson);
        return modelMapper.map(module, ModuleWithLessonsDto.class);
    }

    @Transactional
    public ModuleWithLessonsDto removeLessonFromModule(long moduleId, AddLessonCommand command) {
        Module module = moduleRepositoryOperation.findById(moduleId);
        Lesson lesson = findById(command.getLessonId());
        removeLessonFromModule(module, lesson);
        return modelMapper.map(module, ModuleWithLessonsDto.class);
    }

    public void removeLessonFromModule(Module module, Lesson lesson) {
        if (module.getLessons() == null || !module.getLessons().contains(lesson)) {
            throw new ModuleNotContainsLesson(module, lesson);
        }
        module.getLessons().remove(lesson);
    }


    private Lesson findById(long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        URI.create("lessons/not-found"),
                        "Lesson not found",
                        String.format("Lesson with id %d not found", id)));
    }
}
