package mentortools.module.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.validator.ValidStringLength;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateModuleCommand {

    @NotBlank
    @ValidStringLength
    private String title;

    @NotBlank
    @ValidStringLength
    private String URL;
}
