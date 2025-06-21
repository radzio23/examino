package sigma.examino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sigma.examino.model.Result;
import sigma.examino.model.User;

import java.util.List;
import java.util.UUID;

/**
 * Repozytorium JPA dla encji {@link Result}.
 * Umożliwia wykonywanie operacji CRUD oraz pobieranie wyników użytkownika z powiązanymi egzaminami.
 */
public interface ResultRepository extends JpaRepository<Result, UUID> {

    /**
     * Zwraca listę wyników danego użytkownika, łącznie z informacjami o egzaminach.
     *
     * @param user użytkownik, którego wyniki mają zostać pobrane
     * @return lista wyników powiązanych z użytkownikiem
     */
    @Query("SELECT r FROM Result r JOIN FETCH r.exam WHERE r.user = :user")
    List<Result> findByUser(User user);
}
