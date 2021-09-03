package mentortools.lessoncompletion.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "COMPLETED")
    private PerformStatus videoStatus;

    @NotNull
    @Schema(example = "COMPLETED")
    private PerformStatus taskStatus;

    @NotNull
    private LocalDateTime timeOfVideoWatched;

    @NotNull
    private LocalDateTime timeOfTaskCompleted;

    @ValidStringLength
    @Schema(example = "github.com/abcd/123456789")
    private String commitUrl;

    @Positive
    private long lessonId;

}
