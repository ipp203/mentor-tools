package mentortools.syllabus.model.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.module.model.dto.ModuleDto;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyllabusWithModulesDto {

    private long id;

    private String name;

    private Set<ModuleDto> modules;
}
