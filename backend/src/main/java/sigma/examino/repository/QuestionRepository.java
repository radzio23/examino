package sigma.examino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sigma.examino.model.Question;
import java.util.UUID;


public interface QuestionRepository extends JpaRepository<Question, UUID> {
}
