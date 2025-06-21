package sigma.examino.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import sigma.examino.config.TestSecurityConfig;
import sigma.examino.model.Role;
import sigma.examino.model.User;
import sigma.examino.repository.UserRepository;
import sigma.examino.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testy jednostkowe dla {@link UserController}.
 * Sprawdzają działanie rejestracji, logowania, usuwania użytkowników oraz pobierania listy studentów.
 */
@WebMvcTest(controllers = UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Testuje rejestrację użytkownika — scenariusz pozytywny.
     */
    @Test
    void testRegisterUser_success() throws Exception {
        Map<String, String> request = Map.of("username", "john", "password", "pass123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Użytkownik zarejestrowany"));

        verify(userService).registerUser("john", "pass123", Role.STUDENT);
    }

    /**
     * Testuje rejestrację użytkownika z brakującymi danymi.
     */
    @Test
    void testRegisterUser_missingFields() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Brakuje danych"));
    }

    /**
     * Testuje poprawne logowanie użytkownika.
     */
    @Test
    void testLoginUser_success() throws Exception {
        User user = new User();
        user.setUsername("john");
        user.setRole(Role.STUDENT);

        when(userService.loginUserReturnUser("john", "pass123")).thenReturn(user);

        Map<String, String> request = Map.of("username", "john", "password", "pass123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.role").value("STUDENT"));
    }

    /**
     * Testuje logowanie z nieprawidłowymi danymi.
     */
    @Test
    void testLoginUser_invalid() throws Exception {
        when(userService.loginUserReturnUser("john", "bad")).thenReturn(null);

        Map<String, String> request = Map.of("username", "john", "password", "bad");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Niepoprawny login lub hasło"));
    }

    /**
     * Testuje pobieranie listy studentów.
     */
    @Test
    void testGetAllStudents() throws Exception {
        when(userService.findAllByRole(Role.STUDENT)).thenReturn(List.of());

        mockMvc.perform(get("/api/auth/students"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    /**
     * Testuje poprawne usunięcie użytkownika przez administratora.
     */
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testDeleteUser_success() throws Exception {
        UUID id = UUID.randomUUID();

        when(userRepository.existsById(id)).thenReturn(true);

        mockMvc.perform(delete("/api/auth/" + id))
                .andExpect(status().isNoContent());

        verify(userRepository).deleteById(id);
    }

    /**
     * Testuje próbę usunięcia nieistniejącego użytkownika.
     */
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testDeleteUser_notFound() throws Exception {
        UUID id = UUID.randomUUID();

        when(userRepository.existsById(id)).thenReturn(false);

        mockMvc.perform(delete("/api/auth/" + id))
                .andExpect(status().isNotFound());
    }
}
