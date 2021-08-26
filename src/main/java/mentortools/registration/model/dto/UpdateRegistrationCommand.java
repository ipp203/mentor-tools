package mentortools.registration.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.registration.model.RegistrationStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRegistrationCommand {
    @Positive(message = "Id must be positive!")
    private long studentId;

    @NotNull
    private RegistrationStatus status;
}
