package mentortools.lessoncompletion.repository;

import mentortools.lessoncompletion.model.LessonCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonCompletionRepository extends JpaRepository<LessonCompletion, Long> {

    List<LessonCompletion> findAllByStudentId(long studentId);

    List<LessonCompletion> findAllByLessonId(long lessonId);
}
