package mentortools.lesson.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.module.model.dto.ModuleDto;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {
    private long id;

    private String title;

    private String url;

    private ModuleDto module;
}
