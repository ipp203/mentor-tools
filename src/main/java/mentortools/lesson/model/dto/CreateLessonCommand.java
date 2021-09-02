package mentortools.lesson.model.dto;

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
    private String title;
    @NotBlank
    @ValidStringLength
    private String url;
}
