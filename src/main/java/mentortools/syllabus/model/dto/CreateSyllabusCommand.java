package mentortools.syllabus.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.validator.ValidStringLength;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSyllabusCommand {
    @NotBlank
    @ValidStringLength
    @Schema(example = "JPA")
    private String name;
}
