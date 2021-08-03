package mentortools.trainingclass.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TrainingClassDates {
    @Schema(example = "2021-05-31")
    private LocalDate startDate;

    @Schema(example = "2021-08-02")
    private LocalDate endDate;

    public TrainingClassDates(LocalDate startDate) {
        this.startDate = startDate;
    }
}
