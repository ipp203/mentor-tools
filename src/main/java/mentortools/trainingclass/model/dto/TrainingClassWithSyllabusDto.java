package mentortools.trainingclass.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.syllabus.model.dto.SyllabusDto;
import mentortools.trainingclass.model.TrainingClassDates;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingClassWithSyllabusDto {
    private long id;

    private String name;

    private TrainingClassDates dates;

    private SyllabusDto syllabus;
}
