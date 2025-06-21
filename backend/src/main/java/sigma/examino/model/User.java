package sigma.examino.model;

import jakarta.persistence.*;
import java.util.UUID;

/**
 * Encja reprezentująca użytkownika systemu.
 * Użytkownik posiada unikalny identyfikator, nazwę użytkownika, hasło oraz rolę (STUDENT lub ADMIN).
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Unikalny identyfikator użytkownika typu UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Nazwa użytkownika (login), unikalna w systemie.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Hasło użytkownika, przechowywane w formie zaszyfrowanej.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Rola użytkownika – STUDENT lub ADMIN.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * Zwraca identyfikator użytkownika.
     * @return UUID użytkownika
     */
    public UUID getId() {
        return id;
    }

    /**
     * Ustawia identyfikator użytkownika.
     * @param id UUID użytkownika
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Zwraca nazwę użytkownika.
     * @return login użytkownika
     */
    public String getUsername() {
        return username;
    }

    /**
     * Ustawia nazwę użytkownika.
     * @param username login użytkownika
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Zwraca hasło użytkownika (zaszyfrowane).
     * @return zaszyfrowane hasło
     */
    public String getPassword() {
        return password;
    }

    /**
     * Ustawia zaszyfrowane hasło użytkownika.
     * @param password zaszyfrowane hasło
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Zwraca rolę użytkownika.
     * @return rola (STUDENT lub ADMIN)
     */
    public Role getRole() {
        return role;
    }

    /**
     * Ustawia rolę użytkownika.
     * @param role rola (STUDENT lub ADMIN)
     */
    public void setRole(Role role) {
        this.role = role;
    }
}
