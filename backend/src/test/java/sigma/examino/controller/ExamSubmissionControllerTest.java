package sigma.examino.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import sigma.examino.config.TestSecurityConfig;
import sigma.examino.dto.ExamSubmissionDTO;
import sigma.examino.model.*;
import sigma.examino.repository.*;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Testy jednostkowe dla kontrolera {@link ExamSubmissionController}.
 * Testuje poprawność działania logiki przesyłania rozwiązania egzaminu przez użytkownika.
 */
@WebMvcTest(ExamSubmissionController.class)
@Import({ExamSubmissionControllerTest.MockBeansConfig.class, TestSecurityConfig.class})
public class ExamSubmissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Konfiguracja mockowanych zależności dla testowanego kontrolera.
     */
    @TestConfiguration
    static class MockBeansConfig {
        @Bean public ExamRepository examRepository() { return mock(ExamRepository.class); }
        @Bean public QuestionRepository questionRepository() { return mock(QuestionRepository.class); }
        @Bean public ResultRepository resultRepository() { return mock(ResultRepository.class); }
        @Bean public UserRepository userRepository() { return mock(UserRepository.class); }
    }

    /**
     * Test sprawdza poprawne przesłanie rozwiązań egzaminu przez użytkownika
     * i zapisanie wyniku do repozytorium.
     *
     * Zakłada istnienie:
     * - użytkownika "testuser",
     * - egzaminu o danym ID,
     * - pytania z poprawną odpowiedzią,
     * - przesłanej odpowiedzi odpowiadającej poprawnej.
     *
     * Oczekuje odpowiedzi HTTP 200 OK i wywołania zapisu wyniku.
     */
    @Test
    @WithMockUser(username = "testuser")
    void testSubmitExam_success() throws Exception {
        UUID examId = UUID.randomUUID();
        UUID questionId = UUID.randomUUID();

        Exam exam = new Exam();
        exam.setId(examId);

        Question question = new Question();
        question.setId(questionId);
        question.setContent("2 + 2 = ?");
        question.setCorrectAnswer(1); // "4"
        question.setAnswers(List.of("3", "4", "5"));

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");

        // Dane przesyłane przez użytkownika
        ExamSubmissionDTO submission = new ExamSubmissionDTO();
        submission.setExamId(examId);
        Map<String, String> answers = new HashMap<>();
        answers.put(questionId.toString(), "1");
        submission.setAnswers(answers);

        when(examRepository.findById(examId)).thenReturn(Optional.of(exam));
        when(questionRepository.findByExamId(examId)).thenReturn(List.of(question));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submission)))
                .andExpect(status().isOk());

        verify(resultRepository).save(any(Result.class));
    }
}
