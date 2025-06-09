package sigma.examino.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import sigma.examino.model.Role;
import sigma.examino.model.User;
import sigma.examino.repository.UserRepository;

import java.util.Optional;
import java.util.List;

/**
 * Serwis zarządzający operacjami na użytkownikach,
 * takimi jak rejestracja, logowanie oraz wyszukiwanie użytkowników.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Konstruktor serwisu użytkowników.
     * Inicjalizuje repozytorium oraz enkoder haseł BCrypt.
     *
     * @param userRepository repozytorium użytkowników
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Rejestruje nowego użytkownika.
     * Sprawdza, czy użytkownik o podanej nazwie już istnieje — jeśli tak, rzuca wyjątek.
     * Hasło jest szyfrowane za pomocą BCrypt przed zapisaniem.
     *
     * @param username nazwa użytkownika
     * @param password hasło użytkownika w postaci plaintext
     * @param role     rola użytkownika (np. STUDENT, ADMIN)
     * @return zapisany użytkownik (obiekt {@link User})
     * @throws RuntimeException jeśli użytkownik o podanej nazwie już istnieje
     */
    public User registerUser(String username, String password, Role role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Użytkownik o takiej nazwie już istnieje");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        return userRepository.save(user);
    }

    /**
     * Próba logowania użytkownika.
     * Wyszukuje użytkownika po nazwie użytkownika,
     * jeśli użytkownik nie istnieje, zwraca null.
     * Jeśli istnieje, sprawdza poprawność hasła.
     *
     * @param username nazwa użytkownika
     * @param password hasło w postaci plaintext do porównania
     * @return obiekt {@link User} jeśli dane są poprawne, lub null jeśli niepoprawne
     */
    public User loginUserReturnUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    /**
     * Znajduje użytkownika po nazwie użytkownika.
     *
     * @param username nazwa użytkownika
     * @return {@link Optional} z użytkownikiem lub pusty, jeśli nie znaleziono
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Zwraca listę użytkowników o podanej roli.
     *
     * @param role rola użytkowników do wyszukania
     * @return lista użytkowników z daną rolą
     */
    public List<User> findAllByRole(Role role) {
        return userRepository.findAllByRole(role);
    }

    /**
     * Metoda uruchamiana po starcie aplikacji,
     * tworzy domyślnego administratora, jeśli nie istnieje w bazie.
     */
    @PostConstruct
    public void createDefaultAdmin() {
        String adminUsername = "administrator";
        String adminPassword = "sigma25!";

        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Utworzono domyślnego admina: " + adminUsername);
        }
    }
}
    