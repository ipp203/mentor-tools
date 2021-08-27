package mentortools.syllabus.model.dto;

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
    private String name;
}
