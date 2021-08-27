package mentortools.syllabus.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignSyllabusToTrainingClassCommand {

    @Positive
    private long syllabusId;
}
