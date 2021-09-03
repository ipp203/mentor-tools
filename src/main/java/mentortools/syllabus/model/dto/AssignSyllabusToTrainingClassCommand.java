package mentortools.syllabus.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignSyllabusToTrainingClassCommand {

    @Positive
    @Schema(example = "1")
    private long syllabusId;
}
