package mentortools.registration.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.registration.model.Status;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRegistrationCommand {
    private long studentId;
    @NotNull
    private Status status;
}
