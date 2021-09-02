package mentortools.module.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.lesson.model.dto.LessonDto;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleWithLessonsDto {

    private long id;

    private String title;

    private String url;

    private Set<LessonDto> lessons;
}
