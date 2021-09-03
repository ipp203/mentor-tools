package mentortools.module.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddModuleCommand {
    @Positive(message = "Id must be positive!")
    private long moduleId;
}
