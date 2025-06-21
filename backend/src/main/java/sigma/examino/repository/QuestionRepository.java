package sigma.examino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sigma.examino.model.Question;

import java.util.List;
import java.util.UUID;

/**
 * Repozytorium JPA dla encji {@link Question}.
 * Umożliwia wykonywanie operacji CRUD oraz wyszukiwanie pytań powiązanych z egzaminem.
 */
public interface QuestionRepository extends JpaRepository<Question, UUID> {

    /**
     * Zwraca listę pytań przypisanych do danego egzaminu.
     *
     * @param examId identyfikator egzaminu
     * @return lista pytań należących do egzaminu
     */
    List<Question> findByExamId(UUID examId);
}
