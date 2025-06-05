package sigma.examino.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sigma.examino.model.Role;
import sigma.examino.model.User;
import sigma.examino.repository.UserRepository;

import java.util.Optional;
import java.util.List;
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Konstruktor, w którym inicjalizujemy repozytorium użytkowników oraz enkoder hasła BCrypt
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Rejestruje nowego użytkownika.
     * Sprawdza, czy użytkownik o podanej nazwie już istnieje - jeśli tak, rzuca wyjątek.
     * Hasło jest szyfrowane za pomocą BCrypt.
     * @param username nazwa użytkownika
     * @param password hasło użytkownika (plaintext)
     * @param role rola użytkownika (np. STUDENT lub ADMIN)
     * @return zapisany użytkownik (obiekt User)
     * @throws RuntimeException jeśli użytkownik o podanej nazwie już istnieje
     */
    public User registerUser(String username, String password, Role role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Użytkownik o takiej nazwie już istnieje");
        }

        User user = new User();
        user.setUsername(username);
        // Szyfrujemy hasło przed zapisaniem do bazy
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        return userRepository.save(user);  // zapis do bazy i zwrócenie użytkownika
    }

    /**
     * Próba logowania użytkownika.
     * Wyszukuje użytkownika po nazwie, jeśli go nie ma, zwraca null.
     * Jeśli użytkownik istnieje, sprawdza, czy hasło pasuje do zahashowanego hasła w bazie.
     * @param username nazwa użytkownika
     * @param password hasło w plaintext do porównania
     * @return obiekt User jeśli dane są poprawne, lub null jeśli błędne
     */
    public User loginUserReturnUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return null;  // użytkownik nie istnieje
        }

        User user = userOpt.get();
        // Sprawdzenie poprawności hasła (plaintext vs hashed)
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;  // poprawne dane, zwracamy użytkownika
        }
        return null;  // niepoprawne hasło
    }

    /**
     * Pomocnicza metoda do wyszukiwania użytkownika po nazwie.
     * @param username nazwa użytkownika
     * @return Optional z użytkownikiem lub pusty, jeśli nie znaleziono
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAllByRole(Role role) {
    return userRepository.findAllByRole(role);
    }

}
