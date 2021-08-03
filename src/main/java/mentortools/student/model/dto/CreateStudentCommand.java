package mentortools.student.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.validator.ValidStringLength;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentCommand {

    @NotNull
    @ValidStringLength
    private String name;

    @NotNull
    @Email
    @ValidStringLength
    private String email;

    private String gitHubUserName;

    private String comment;
}
