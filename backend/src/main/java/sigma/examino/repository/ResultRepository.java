package sigma.examino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sigma.examino.model.Result;
import java.util.UUID;

public interface ResultRepository extends JpaRepository<Result, UUID> {
}
