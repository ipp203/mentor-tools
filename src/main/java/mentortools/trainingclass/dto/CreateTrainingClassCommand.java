package mentortools.trainingclass.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.validator.ValidStringLength;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTrainingClassCommand {

    @NotBlank
    @ValidStringLength(length = 255)
    @Schema(example = "Strukturavaltok")
    private String name;

    @NotNull
    @Schema(example = "2020-10-28")
    private LocalDate startDate;
}
