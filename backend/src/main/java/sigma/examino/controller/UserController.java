package sigma.examino.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sigma.examino.model.Role;
import sigma.examino.service.UserService;

import java.util.Map;

/**
 * Kontroler obsługujący endpointy związane z autoryzacją użytkowników, naszą rejestrację i logowanie.
 */
@RestController
@RequestMapping("/api/auth")  // Bazowy URL dla tego kontrolera to /api/auth
public class UserController {

    private final UserService userService;

    /**
     * Konstruktor
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint do rejestracji nowego użytkownika.
     * Potrzebuje on user, password i role
     *
     * @param body mapa z danymi w fromacie username, password, role
     * @return odpowiedz z potwierdzeniem lub info o bledzie
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String roleStr = body.get("role");

        // Sprawdzamy, czy wszytskie dane przesłalismy
        if (username == null || password == null || roleStr == null) {
            return ResponseEntity.badRequest().body("Brakuje danych");
        }

        // parsowanie naszej roli
        Role role;
        try {
            role = Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Niepoprawna rola");
        }

        // Rejestracja naszego uzytkownika, jak istnieje to mamy błąd.
        try {
            userService.registerUser(username, password, role);
            return ResponseEntity.ok("Użytkownik zarejestrowany");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint do logowania użytkownika.
     * Oczekujemy pól username, password.
     *
     * @param body mapa z danymi username, password
     * @return odpowiedz czy sie udało czy też nie.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        // Sprawdzamy czy mamy wszytskie dane.
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body("Brakuje danych");
        }

        // Sprawdzamy login oraz haasło, czy są poprawne.
        boolean loggedIn = userService.loginUser(username, password);
        if (loggedIn) {
            return ResponseEntity.ok("Zalogowano pomyślnie");
        } else {
            return ResponseEntity.status(401).body("Niepoprawny login lub hasło");
        }
    }
}
