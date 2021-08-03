package mentortools.trainingclass.repository;

import mentortools.trainingclass.model.TrainingClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingClassRepository extends JpaRepository<TrainingClass, Long> {

}
