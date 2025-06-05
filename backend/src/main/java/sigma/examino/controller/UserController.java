package sigma.examino.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sigma.examino.model.Role;
import sigma.examino.model.User;
import sigma.examino.service.UserService;

import java.util.Map;

/**
 * Kontroler obsługujący endpointy związane z autoryzacją użytkowników,
 * czyli rejestrację i logowanie.
 */
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    // Konstruktor - wstrzykiwanie serwisu UserService
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint POST /api/auth/register - rejestracja nowego użytkownika.
     * Oczekuje JSON z polami: username, password, role.
     * Sprawdza, czy podane dane są kompletne i czy rola jest poprawna.
     * Rejestruje użytkownika za pomocą serwisu.
     * Zwraca odpowiedź 200 OK z komunikatem sukcesu lub 400 z błędem.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String roleStr = body.get("role");

        // Sprawdzenie czy dane są obecne
        if (username == null || password == null || roleStr == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Brakuje danych"));
        }

        Role role;
        try {
            // Konwersja tekstu na enum Role, z uwzględnieniem wielkości liter
            role = Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Niepoprawna rola w zapytaniu
            return ResponseEntity.badRequest().body(Map.of("error", "Niepoprawna rola"));
        }

        try {
            // Próba rejestracji użytkownika
            userService.registerUser(username, password, role);
            return ResponseEntity.ok(Map.of("message", "Użytkownik zarejestrowany"));
        } catch (RuntimeException e) {
            // Jeśli użytkownik o takim username już istnieje lub inny błąd
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint POST /api/auth/login - logowanie użytkownika.
     * Oczekuje JSON z polami: username i password.
     * Sprawdza poprawność danych, a jeśli dane się zgadzają,
     * zwraca nazwę użytkownika i jego rolę w odpowiedzi.
     * W przeciwnym razie zwraca 401 Unauthorized.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        // Sprawdzenie czy dane są obecne
        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Brakuje danych"));
        }

        // Próba zalogowania użytkownika
        User user = userService.loginUserReturnUser(username, password);
        if (user != null) {
            // Zwróć dane użytkownika (np. dla frontendu do rozróżnienia roli)
            return ResponseEntity.ok(Map.of(
                "username", user.getUsername(),
                "role", user.getRole().toString()
            ));
        } else {
            // Błędne dane logowania
            return ResponseEntity.status(401).body(Map.of("error", "Niepoprawny login lub hasło"));
        }
    }

    @GetMapping("/students")
    public ResponseEntity<?> getAllStudents() {
        return ResponseEntity.ok(
            userService.findAllByRole(Role.STUDENT)
        );
    }

}
