package mentortools.trainingclass.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.trainingclass.model.TrainingClassDates;
import mentortools.validator.ValidDates;
import mentortools.validator.ValidStringLength;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrainingClassCommand {

    @ValidStringLength()
    @Schema(example = "Strukturavalto 2.")
    private String name;

    @ValidDates
    private TrainingClassDates dates;
}
