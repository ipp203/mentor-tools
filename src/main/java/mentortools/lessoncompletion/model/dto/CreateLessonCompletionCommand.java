package mentortools.lessoncompletion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.lessoncompletion.model.PerformStatus;
import mentortools.validator.ValidStringLength;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLessonCompletionCommand {

    @NotNull
    private PerformStatus videoStatus;

    @NotNull
    private PerformStatus taskStatus;

    @NotNull
    private LocalDateTime timeOfVideoWatched;

    @NotNull
    private LocalDateTime timeOfTaskCompleted;

    @ValidStringLength
    private String commitUrl;

    @Positive
    private long lessonId;

    @Positive
    private long studentId;
}
