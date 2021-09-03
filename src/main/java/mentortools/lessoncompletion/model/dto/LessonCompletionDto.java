package mentortools.lessoncompletion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.lesson.model.dto.LessonDto;
import mentortools.lessoncompletion.model.TaskCompletion;
import mentortools.lessoncompletion.model.VideoCompletion;
import mentortools.student.model.dto.StudentDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonCompletionDto {
    private long id;

    private VideoCompletion videoCompletion;

    private TaskCompletion taskCompletion;

    private LessonDto lesson;

    private StudentDto student;
}
