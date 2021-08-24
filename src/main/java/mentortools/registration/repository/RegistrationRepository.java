package mentortools.registration.repository;

import mentortools.registration.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    @Query("select r from Registration r where r.student.id = :studentId and r.trainingClass.id = :trainingid")
    Optional<Registration> findRegistrationByStudentIdAndTrainingClassId(long studentId, long trainingId);
}
