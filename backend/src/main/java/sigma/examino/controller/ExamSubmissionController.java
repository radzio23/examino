package sigma.examino.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sigma.examino.dto.ExamResultDTO;
import sigma.examino.dto.ExamResultDetailDTO;
import sigma.examino.dto.ExamSubmissionDTO;
import sigma.examino.model.*;
import sigma.examino.repository.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Kontroler obsługujący przesyłanie rozwiązań egzaminów przez użytkowników.
 * Umożliwia ocenę odpowiedzi i zapisanie wyniku w bazie danych.
 */
@RestController
@RequestMapping("/api/submit")
public class ExamSubmissionController {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Endpoint do przesłania rozwiązanego egzaminu przez użytkownika.
     * Sprawdza poprawność odpowiedzi, oblicza wynik i zapisuje go w bazie.
     *
     * @param submission obiekt zawierający ID egzaminu oraz mapę odpowiedzi użytkownika
     * @param principal obiekt reprezentujący aktualnie zalogowanego użytkownika
     * @return obiekt DTO z wynikiem egzaminu lub kod błędu w przypadku niepowodzenia
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> submitExam(@RequestBody ExamSubmissionDTO submission, Principal principal) {
        try {
            UUID examId = submission.getExamId();
            Map<String, String> userAnswersRaw = submission.getAnswers();
            Map<UUID, String> userAnswers = new HashMap<>();

            // Parsowanie odpowiedzi z String (UUID jako String) na UUID
            for (Map.Entry<String, String> entry : userAnswersRaw.entrySet()) {
                try {
                    UUID questionId = UUID.fromString(entry.getKey());
                    userAnswers.put(questionId, entry.getValue());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "Nieprawidłowy format UUID: " + entry.getKey()));
                }
            }

            // Pobranie egzaminu z bazy
            Exam exam = examRepository.findById(examId)
                    .orElseThrow(() -> new RuntimeException("Egzamin nie istnieje"));

            // Pobranie pytań przypisanych do egzaminu
            List<Question> questions = questionRepository.findByExamId(examId);
            if (questions.isEmpty()) {
                throw new RuntimeException("Brak pytań dla egzaminu");
            }

            int correct = 0;
            List<ExamResultDetailDTO> details = new ArrayList<>();

            // Sprawdzenie poprawności odpowiedzi
            for (Question q : questions) {
                ExamResultDetailDTO detail = new ExamResultDetailDTO();
                detail.setQuestionId(q.getId());
                detail.setQuestionContent(q.getContent());
                detail.setAnswers(q.getAnswers());
                detail.setCorrectAnswer(q.getCorrectAnswer());

                String userAnswerStr = userAnswers.get(q.getId());
                Integer userAnswer = null;
                boolean isCorrect = false;

                if (userAnswerStr != null) {
                    try {
                        userAnswer = Integer.parseInt(userAnswerStr);
                        if (userAnswer == q.getCorrectAnswer()) {
                            correct++;
                            isCorrect = true;
                        }
                    } catch (NumberFormatException ignored) {
                        // Niepoprawny format odpowiedzi
                    }
                }

                detail.setUserAnswer(userAnswer);
                detail.setCorrect(isCorrect);
                details.add(detail);
            }

            // Obliczenie wyniku procentowego
            float score = (correct * 100.0f) / questions.size();

            // Pobranie użytkownika z bazy
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Użytkownik nie istnieje"));

            // Zapisanie wyniku w bazie
            Result result = new Result();
            result.setUser(user);
            result.setExam(exam);
            result.setScore(score);
            result.setTimestamp(LocalDateTime.now());
            resultRepository.save(result);

            // Utworzenie DTO z wynikiem i szczegółami
            ExamResultDTO resultDto = new ExamResultDTO();
            resultDto.setScore(score);
            resultDto.setCorrectCount(correct);
            resultDto.setTotal(questions.size());
            resultDto.setDetails(details);

            return ResponseEntity.ok(resultDto);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
