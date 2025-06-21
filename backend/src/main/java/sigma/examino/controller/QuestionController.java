package sigma.examino.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sigma.examino.model.Question;
import sigma.examino.repository.QuestionRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Kontroler REST odpowiedzialny za operacje CRUD na pytaniach egzaminacyjnych.
 * Udostępnia endpointy do pobierania, tworzenia, edytowania i usuwania pytań.
 */
@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * Zwraca listę wszystkich pytań w systemie.
     *
     * @return lista wszystkich pytań
     */
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    /**
     * Zwraca listę pytań przypisanych do danego egzaminu.
     *
     * @param examId identyfikator egzaminu
     * @return lista pytań należących do egzaminu
     */
    @GetMapping("/exam/{examId}")
    public List<Question> getQuestionsByExam(@PathVariable UUID examId) {
        return questionRepository.findByExamId(examId);
    }

    /**
     * Zwraca pytanie o podanym identyfikatorze.
     *
     * @param id identyfikator pytania
     * @return pytanie, jeśli istnieje; w przeciwnym razie kod 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable UUID id) {
        Optional<Question> question = questionRepository.findById(id);
        return question.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Tworzy nowe pytanie i zapisuje je w bazie danych.
     *
     * @param question obiekt pytania do zapisania (konwertowany z JSON)
     * @return utworzony obiekt pytania
     */
    @PostMapping
    public Question createQuestion(@RequestBody Question question) {
        return questionRepository.save(question);
    }

    /**
     * Aktualizuje istniejące pytanie.
     *
     * @param id identyfikator pytania z URL
     * @param updatedQuestion nowe dane pytania (z JSON-a)
     * @return zaktualizowane pytanie lub kod 404, jeśli nie znaleziono
     */
    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable UUID id, @Valid @RequestBody Question updatedQuestion) {
        return questionRepository.findById(id)
                .map(existingQuestion -> {
                    existingQuestion.setContent(updatedQuestion.getContent());
                    existingQuestion.setExam(updatedQuestion.getExam());
                    existingQuestion.setAnswers(updatedQuestion.getAnswers());
                    existingQuestion.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
                    Question saved = questionRepository.save(existingQuestion);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Usuwa pytanie o podanym identyfikatorze.
     *
     * @param id identyfikator pytania
     * @return odpowiedź 204 jeśli usunięto, lub 404 jeśli pytanie nie istnieje
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable UUID id) {
        if (!questionRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        questionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
