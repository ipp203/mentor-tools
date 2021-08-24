package mentortools.registration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.student.model.Student;
import mentortools.trainingclass.model.TrainingClass;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Student student;

    @OneToOne
    private TrainingClass trainingClass;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Registration(Student student, TrainingClass trainingClass, Status status) {
        this.student = student;
        this.trainingClass = trainingClass;
        this.status = status;
    }
}
