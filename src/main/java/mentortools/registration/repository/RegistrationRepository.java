package mentortools.registration.repository;

import mentortools.registration.model.Registration;
import mentortools.registration.model.dto.RegisteredStudentDto;
import mentortools.registration.model.dto.TrainingClassNameDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    @Query("select r from Registration r where r.student.id = :studentId and r.trainingClass.id = :trainingId")
    Optional<Registration> findRegistrationByStudentIdAndTrainingClassId(long studentId, long trainingId);

    @Query("select new mentortools.registration.model.dto.RegisteredStudentDto(r.student.id, r.student.name, r.registrationStatus) " +
            "from Registration r where r.trainingClass.id = :trainingId")
    List<RegisteredStudentDto> findRegisteredStudentsByTrainingId(long trainingId);

    @Query("select new mentortools.registration.model.dto.TrainingClassNameDto(r.trainingClass.id,r.trainingClass.name)" +
            "from Registration r where r.student.id = :studentId")
    List<TrainingClassNameDto> findTrainingClassesByStudentId(long studentId);
}
