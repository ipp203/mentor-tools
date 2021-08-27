package mentortools.syllabus.repository;

import mentortools.syllabus.model.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SyllabusRepository extends JpaRepository<Syllabus, Long> {

}
