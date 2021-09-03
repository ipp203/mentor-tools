package mentortools.module.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "JPA")
    private String title;

    @NotBlank
    @ValidStringLength
    @Schema(example = "api/modules/jpa")
    private String url;
}
