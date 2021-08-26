package mentortools.student.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Name of student", required = true, example = "John Doe")
    private String name;

    @NotNull
    @Email
    @ValidStringLength
    @Schema(description = "Email of student", required = true, example = "john.doe@johndoe.hu")
    private String email;

    @Schema(description = "Student's username on github", example = "johnydoe")
    private String gitHubUserName;

    @Schema(description = "Any comment to the student", example = "The cleverest student in the world.")
    private String comment;
}
