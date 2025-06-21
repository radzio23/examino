package sigma.examino.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import sigma.examino.model.Result;
import sigma.examino.model.User;
import sigma.examino.repository.ResultRepository;
import sigma.examino.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testy jednostkowe dla kontrolera {@link ResultController}.
 * Sprawdzają poprawność działania endpointu zwracającego wyniki użytkownika.
 */
@WebMvcTest(ResultController.class)
class ResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResultRepository resultRepository;

    @MockBean
    private UserRepository userRepository;

    /**
     * Testuje endpoint GET /api/results/my.
     * Sprawdza, czy poprawnie zwracana jest pusta lista wyników dla użytkownika "testuser".
     * Zakłada, że użytkownik istnieje i nie ma przypisanych wyników.
     */
    @WithMockUser(username = "testuser")
    @Test
    void testGetMyResults() throws Exception {
        // Tworzymy użytkownika
        User user = new User();
        user.setUsername("testuser");

        // Zwracamy użytkownika i pustą listę wyników
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(resultRepository.findByUser(user)).thenReturn(List.of());

        // Sprawdzamy, że dostajemy 200 OK i pustą listę []
        mockMvc.perform(get("/api/results/my"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
