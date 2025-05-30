package sigma.examino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sigma.examino.model.User;
import java.util.UUID;
import java.util.Optional;

/**
 * Interfejs UserRepository służy do operacji na encji User w bazie danych.
 * Rozszerza JpaRepository, co pozwala na korzystanie z gotowych metod do
 * podstawowych operacji CRUD
 *
 * Typ encji: User
 * Typ klucza głównego: UUID
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Metoda pozwala znaleźć użytkownika po jego nazwie (username).
     * Zwraca Optional<User>, ponieważ użytkownik o danej nazwie może, ale nie musi istnieć.
     *
     * @param username nazwa użytkownika, po której chcemy wyszukać
     * @return Optional zawierający znalezionego użytkownika lub pusty, jeśli nie istnieje
     */
    Optional<User> findByUsername(String username);
}
