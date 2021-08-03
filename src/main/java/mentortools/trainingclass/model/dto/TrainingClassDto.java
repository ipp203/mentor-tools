package mentortools.trainingclass.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.trainingclass.model.TrainingClassDates;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingClassDto {
    private long id;

    private String name;

    private TrainingClassDates dates;
}
