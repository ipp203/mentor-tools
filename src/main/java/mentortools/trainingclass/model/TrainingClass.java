package mentortools.trainingclass.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(length = 355)
    private String name;

    private TrainingClassDates dates;

    public TrainingClass(String name, LocalDate startDate) {
        this.name = name;
        this.dates = new TrainingClassDates(startDate);
    }


}
