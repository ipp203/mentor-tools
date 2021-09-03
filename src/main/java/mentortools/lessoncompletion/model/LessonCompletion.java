package mentortools.lessoncompletion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.lesson.model.Lesson;
import mentortools.student.model.Student;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lesson_completions")
public class LessonCompletion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    private VideoCompletion videoCompletion;

    @Embedded
    private TaskCompletion taskCompletion;

    @OneToOne
    private Lesson lesson;

    @OneToOne
    private Student student;

    public LessonCompletion(VideoCompletion videoCompletion, TaskCompletion taskCompletion, Lesson lesson, Student student) {
        this.videoCompletion = videoCompletion;
        this.taskCompletion = taskCompletion;
        this.lesson = lesson;
        this.student = student;
    }
}
