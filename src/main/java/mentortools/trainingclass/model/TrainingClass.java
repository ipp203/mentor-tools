package mentortools.trainingclass.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mentortools.syllabus.model.Syllabus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "training_classes")
public class TrainingClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 255)
    private String name;

    private TrainingClassDates dates;

    @ManyToOne
    private Syllabus syllabus;

    public TrainingClass(String name, LocalDate startDate) {
        this.name = name;
        this.dates = new TrainingClassDates(startDate);
    }


}
