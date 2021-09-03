package mentortools.lessoncompletion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class  VideoCompletion {

    @Enumerated(value = EnumType.STRING)
    private PerformStatus videoStatus;

    private LocalDateTime timeOfVideoWatched;
}
