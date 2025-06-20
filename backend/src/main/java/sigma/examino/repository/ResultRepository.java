package sigma.examino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sigma.examino.model.Result;
import sigma.examino.model.User;
import java.util.List;
import java.util.UUID;

public interface ResultRepository extends JpaRepository<Result, UUID> {
    @Query("SELECT r FROM Result r JOIN FETCH r.exam WHERE r.user = :user")
    List<Result> findByUser(User user);
}
