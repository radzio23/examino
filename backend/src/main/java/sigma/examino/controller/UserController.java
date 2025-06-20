package sigma.examino.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sigma.examino.model.Role;
import sigma.examino.model.User;
//import sigma.examino.repository.ExamRepository;
import sigma.examino.repository.UserRepository;
import sigma.examino.security.JwtUtils;
import sigma.examino.service.UserService;

import java.util.Map;
import java.util.UUID;

/**
 * Kontroler obsługujący endpointy związane z autoryzacją użytkowników,
 * takie jak rejestracja, logowanie oraz pobieranie listy studentów.
 *
 */
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * Konstruktor wstrzykujący serwis użytkownika.
     *
     * @param userService serwis użytkownika
     */
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Endpoint POST /api/auth/register – rejestracja nowego użytkownika.
     * Oczekuje mapy JSON zawierającej klucze username i password
     * Nowemu użytkownikowi zawsze przypisywana jest rola STUDENT
     *
     * @param body mapa zawierająca dane rejestracyjne (username, password)
     * @return odpowiedź HTTP 200 po pomyślnej rejestracji lub 400 w przypadku błędu
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Brakuje danych"));
        }

        Role role = Role.STUDENT;

        try {
            userService.registerUser(username, password, role);
            return ResponseEntity.ok(Map.of("message", "Użytkownik zarejestrowany"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint POST /api/auth/login – logowanie użytkownika.
     * Oczekuje mapy JSON zawierającej username i password
     * Po pomyślnym logowaniu zwraca token JWT
     *
     * @param body mapa zawierająca dane logowania
     * @return odpowiedź HTTP 200 z tokenem lub 401 przy błędnych danych
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Brakuje danych"));
        }

        User user = userService.loginUserReturnUser(username, password);
        if (user != null) {
            String token = JwtUtils.generateToken(user.getUsername(), user.getRole().toString());
            return ResponseEntity.ok(Map.of(
                "token", token,
                "role", user.getRole().toString() 
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Niepoprawny login lub hasło"));
        }
    }
    /**
     * Endpoint GET /api/auth/student – pobiera listę wszystkich użytkowników z rolą STUDENT.
     *
     * @return lista studentów w odpowiedzi HTTP 200
     */
    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(userService.findAllByRole(Role.STUDENT));
    }


    /**
     * usuwa użytkownika o podanym ID.
     *
     * @param id identyfikator użytkownika do usunięcia
     * @return odpowiedź HTTP 200 po usunięciu lub 404 jeśli nie znaleziono
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
