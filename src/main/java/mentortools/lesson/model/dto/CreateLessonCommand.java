package mentortools.lesson.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.validator.ValidStringLength;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLessonCommand {

    @NotBlank
    @ValidStringLength
    @Schema(example = "JPA bevezeto")
    private String title;

    @NotBlank
    @ValidStringLength
    @Schema(example = "/api/lessons/jpabev")
    private String url;
}
