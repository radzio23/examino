package sigma.examino.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sigma.examino.model.Role;
import sigma.examino.model.User;
import sigma.examino.repository.UserRepository;

import java.util.Optional;

/**
 * Serwis odpowiedzialny za logikę dotyczącą użytkowników: rejestrację i logowanie.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Tworzymy encoder hasel i repozytorium uzytkownikow.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // encoder do hasła
    }

    /**
     * Rejestruje nowego użytkownika:
     * - Sprawdzamy sobie, czy użytkownik o podanej nazwie już istnieje.
     * - Jak go nie mamy, tworzymy nowego użytkownika z zahashowanym hasłem i rolą i nastepnie zapisujemy.
     *
     * @param username nazwa użytkownika
     * @param password hasło użytkownika
     * @param role rola użytkownika (STUDENT, ADMIN)
     * @return User - nasz użytkownik.
     * @throws RuntimeException gdy użytkownik o podanej nazwie już istnieje
     */
    public User registerUser(String username, String password, Role role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Użytkownik o takiej nazwie już istnieje");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // hasło szyfrujemy
        user.setRole(role);

        return userRepository.save(user);
    }

    /**
     * Logowanie użytkownika:
     * - Sprawdzamy sobie, czy użytkownik o danej nazwie istnieje.
     * - Jak tak, to porównujemy podane hasło z zahashowanym hasłem w bazie.
     * - True, jak hasło sie zgadza, inaczej false.
     *
     * @param username nazwa użytkownika
     * @param password hasło w postaci zahasowanej
     * @return true/false
     */
    public boolean loginUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        return passwordEncoder.matches(password, user.getPassword());
    }
}
