package mentortools.registration.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.registration.model.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredStudentDto {
    private long studentId;
    private String name;
    private Status status;
}
