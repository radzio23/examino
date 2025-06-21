package sigma.examino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sigma.examino.model.Exam;

import java.util.List;
import java.util.UUID;

/**
 * Repozytorium JPA dla encji {@link Exam}.
 * Umożliwia wykonywanie operacji CRUD oraz zapytań niestandardowych.
 */
public interface ExamRepository extends JpaRepository<Exam, UUID> {

    /**
     * Zwraca listę egzaminów, których dany użytkownik jeszcze nie rozwiązał.
     *
     * @param userId identyfikator użytkownika
     * @return lista egzaminów nieukończonych przez użytkownika
     */
    @Query("SELECT e FROM Exam e WHERE e.id NOT IN " +
            "(SELECT r.exam.id FROM Result r WHERE r.user.id = :userId)")
    List<Exam> findExamsNotTakenByUser(@Param("userId") UUID userId);
}
