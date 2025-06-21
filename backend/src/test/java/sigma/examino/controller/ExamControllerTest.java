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
import sigma.examino.model.Exam;
import sigma.examino.model.User;
import sigma.examino.repository.ExamRepository;
import sigma.examino.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Testy jednostkowe dla kontrolera {@link ExamController}.
 * Pokrywają operacje CRUD na egzaminach oraz logikę filtrowania egzaminów,
 * których użytkownik jeszcze nie rozwiązał.
 */
@WebMvcTest(ExamController.class)
@Import({ExamControllerTest.MockBeansConfig.class, TestSecurityConfig.class})
public class ExamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private UserRepository userRepository;

    @TestConfiguration
    static class MockBeansConfig {
        @Bean
        public ExamRepository examRepository() {
            return mock(ExamRepository.class);
        }

        @Bean
        public UserRepository userRepository() {
            return mock(UserRepository.class);
        }
    }

    /**
     * Test sprawdza poprawność zwracania egzaminów niezdanych jeszcze przez użytkownika.
     */
    @WithMockUser(username = "testuser", roles = "ADMIN")
    @Test
    void testGetAllExams() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setUsername("testuser");
        user.setId(userId);

        Principal principal = () -> "testuser";

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(examRepository.findExamsNotTakenByUser(userId)).thenReturn(List.of());

        mockMvc.perform(get("/api/exams").principal(principal))
                .andExpect(status().isOk());
    }

    /**
     * Test sprawdza, czy można pobrać egzamin po ID, jeśli egzamin istnieje.
     */
    @WithMockUser(roles = "ADMIN")
    @Test
    void testGetExamByIdExists() throws Exception {
        UUID examId = UUID.randomUUID();
        Exam exam = new Exam();
        exam.setId(examId);
        exam.setName("Test Exam");

        when(examRepository.findById(examId)).thenReturn(Optional.of(exam));

        mockMvc.perform(get("/api/exams/" + examId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Exam"));
    }

    /**
     * Test sprawdza odpowiedź 404 Not Found, gdy egzamin o podanym ID nie istnieje.
     */
    @WithMockUser(roles = "ADMIN")
    @Test
    void testGetExamByIdNotFound() throws Exception {
        UUID examId = UUID.randomUUID();
        when(examRepository.findById(examId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/exams/" + examId))
                .andExpect(status().isNotFound());
    }

    /**
     * Test sprawdza poprawne utworzenie nowego egzaminu.
     */
    @WithMockUser(roles = "ADMIN")
    @Test
    void testCreateExam() throws Exception {
        Exam exam = new Exam();
        exam.setName("New Exam");
        exam.setSubject("Math");

        when(examRepository.save(any(Exam.class))).thenReturn(exam);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/exams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exam)))
                .andExpect(status().isCreated());
    }

    /**
     * Test sprawdza aktualizację istniejącego egzaminu.
     */
    @WithMockUser(roles = "ADMIN")
    @Test
    void testUpdateExamExists() throws Exception {
        UUID examId = UUID.randomUUID();
        Exam existingExam = new Exam();
        existingExam.setId(examId);
        existingExam.setName("Old Exam");

        Exam updatedExam = new Exam();
        updatedExam.setName("Updated Exam");

        when(examRepository.findById(examId)).thenReturn(Optional.of(existingExam));
        when(examRepository.save(any(Exam.class))).thenReturn(updatedExam);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(put("/api/exams/" + examId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedExam)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Exam"));
    }

    /**
     * Test sprawdza odpowiedź 404 Not Found podczas aktualizacji nieistniejącego egzaminu.
     */
    @WithMockUser(roles = "ADMIN")
    @Test
    void testUpdateExamNotFound() throws Exception {
        UUID examId = UUID.randomUUID();
        Exam updatedExam = new Exam();
        updatedExam.setName("Updated Exam");

        when(examRepository.findById(examId)).thenReturn(Optional.empty());

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(put("/api/exams/" + examId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedExam)))
                .andExpect(status().isNotFound());
    }

    /**
     * Test sprawdza poprawne usunięcie egzaminu, jeśli istnieje.
     */
    @WithMockUser(roles = "ADMIN")
    @Test
    void testDeleteExamExists() throws Exception {
        UUID examId = UUID.randomUUID();
        when(examRepository.existsById(examId)).thenReturn(true);

        mockMvc.perform(delete("/api/exams/" + examId))
                .andExpect(status().isNoContent());

        verify(examRepository).deleteById(examId);
    }

    /**
     * Test sprawdza odpowiedź 404 Not Found przy próbie usunięcia nieistniejącego egzaminu.
     */
    @WithMockUser(roles = "ADMIN")
    @Test
    void testDeleteExamNotFound() throws Exception {
        UUID examId = UUID.randomUUID();
        when(examRepository.existsById(examId)).thenReturn(false);

        mockMvc.perform(delete("/api/exams/" + examId))
                .andExpect(status().isNotFound());
    }
}
