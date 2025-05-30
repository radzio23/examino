package sigma.examino.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    // Unikalny identyfikator użytkownika typu UUID
    private UUID id;

    @Column(nullable = false, unique = true)
    // Nazwa użytkownika
    private String username;

    @Column(nullable = false)
    // Hasło użytkownika, zaszyfrowane
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    // Rola użytkownika, STUDENT lub ADMIN
    private Role role;

    // getter id
    public UUID getId() {
        return id;
    }
    // setter id
    public void setId(UUID id) {
        this.id = id;
    }

    // getter nazwy użytkownika
    public String getUsername() {
        return username;
    }
    // setter nazwy użytkownika
    public void setUsername(String username) {
        this.username = username;
    }

    // getter hasła
    public String getPassword() {
        return password;
    }
    // setter hasła
    public void setPassword(String password) {
        this.password = password;
    }

    // getter roli
    public Role getRole() {
        return role;
    }
    // setter roli
    public void setRole(Role role) {
        this.role = role;
    }
}
