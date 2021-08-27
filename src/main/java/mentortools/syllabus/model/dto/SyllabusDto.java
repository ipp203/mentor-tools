package mentortools.syllabus.model.dto;

import lombok.*;
import mentortools.trainingclass.model.dto.TrainingClassDto;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyllabusDto {

    private long id;

    private String name;

    private Set<TrainingClassDto> trainingClasses;
}
