package mentortools.student.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private long id;

    private String name;

    private String email;

    private String gitHubUserName;

    private String comment;
}
