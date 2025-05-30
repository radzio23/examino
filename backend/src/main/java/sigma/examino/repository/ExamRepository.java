package sigma.examino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sigma.examino.model.Exam;
import java.util.UUID;

public interface ExamRepository extends JpaRepository<Exam, UUID> {}
