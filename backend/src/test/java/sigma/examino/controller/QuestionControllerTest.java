package sigma.examino.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import sigma.examino.config.TestSecurityConfig;
import sigma.examino.model.Question;
import sigma.examino.repository.QuestionRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testy jednostkowe dla kontrolera {@link QuestionController}.
 * Sprawdzają poprawność działania operacji CRUD na pytaniach egzaminacyjnych.
 */
@WebMvcTest(QuestionController.class)
@Import({QuestionControllerTest.MockBeansConfig.class, TestSecurityConfig.class})
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * Konfiguracja testowa dostarczająca mock {@link QuestionRepository}.
     */
    @TestConfiguration
    static class MockBeansConfig {
        @Bean
        public QuestionRepository questionRepository() {
            return mock(QuestionRepository.class);
        }
    }

    /**
     * Testuje endpoint GET /api/questions.
     * Oczekuje poprawnej odpowiedzi 200 OK nawet dla pustej listy.
     */
    @WithMockUser
    @Test
    void testGetAllQuestions() throws Exception {
        when(questionRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/questions"))
                .andExpect(status().isOk());
    }

    /**
     * Testuje sytuację, gdy pytanie o podanym ID nie istnieje.
     * Endpoint GET /api/questions/{id} powinien zwrócić 404.
     */
    @WithMockUser
    @Test
    void testGetQuestionByIdNotFound() throws Exception {
        UUID questionId = UUID.randomUUID();
        when(questionRepository.findById(questionId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/questions/" + questionId))
                .andExpect(status().isNotFound());
    }

    /**
     * Testuje tworzenie nowego pytania przez POST /api/questions.
     * Oczekuje odpowiedzi 200 OK oraz poprawnej treści pytania w odpowiedzi.
     */
    @WithMockUser
    @Test
    void testCreateQuestion() throws Exception {
        Question q = new Question();
        q.setContent("Sample?");
        q.setCorrectAnswer(0);

        when(questionRepository.save(any(Question.class))).thenReturn(q);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(q)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Sample?"));
    }

    /**
     * Testuje próbę usunięcia pytania, które nie istnieje.
     * Endpoint DELETE /api/questions/{id} powinien zwrócić 404.
     */
    @WithMockUser
    @Test
    void testDeleteQuestionNotFound() throws Exception {
        UUID questionId = UUID.randomUUID();
        when(questionRepository.existsById(questionId)).thenReturn(false);

        mockMvc.perform(delete("/api/questions/" + questionId))
                .andExpect(status().isNotFound());
    }
}
