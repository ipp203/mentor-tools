package mentortools.registration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.student.model.Student;
import mentortools.trainingclass.model.TrainingClass;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "registrations")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Student student;

    @OneToOne
    private TrainingClass trainingClass;

    @Enumerated(EnumType.STRING)
    private RegistrationStatus registrationStatus;

    public Registration(Student student, TrainingClass trainingClass, RegistrationStatus registrationStatus) {
        this.student = student;
        this.trainingClass = trainingClass;
        this.registrationStatus = registrationStatus;
    }
}
