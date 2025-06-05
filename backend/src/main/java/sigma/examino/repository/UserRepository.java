package sigma.examino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sigma.examino.model.Role;
import sigma.examino.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interfejs UserRepository służy do operacji na encji User w bazie danych.
 * Rozszerza JpaRepository, co pozwala na korzystanie z gotowych metod do
 * podstawowych operacji CRUD.
 *
 * Typ encji: User
 * Typ klucza głównego: UUID
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Znajdź użytkownika po jego nazwie użytkownika (username).
     * Zwraca Optional<User>, ponieważ użytkownik może nie istnieć.
     *
     * @param username nazwa użytkownika, po której chcemy wyszukać
     * @return Optional zawierający użytkownika, jeśli istnieje
     */
    Optional<User> findByUsername(String username);

    /**
     * Znajdź wszystkich użytkowników posiadających określoną rolę (np. STUDENT).
     *
     * @param role rola użytkowników do wyszukania
     * @return lista użytkowników o podanej roli
     */
    List<User> findAllByRole(Role role);
}
