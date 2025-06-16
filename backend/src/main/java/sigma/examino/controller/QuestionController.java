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

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * Zwraca listę wszystkich pytań.
     *
     * @return lista pytań
     */
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    /**
     * Zwraca pytania danego egzaminu.
     *
     * @param examId id egzaminu
     * @return pytania z danego egzaminu
     */
    @GetMapping("/exam/{examId}")
    public List<Question> getQuestionsByExam(@PathVariable UUID examId) {
        return questionRepository.findByExamId(examId);
    }

    /**
     * Zwraca pytanie o danum id.
     *
     * @param examId id pytania
     * @return jeśli pytanie istnieje to je zwraca, jeśli nie kod 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable UUID id) {
        Optional<Question> question = questionRepository.findById(id);
        return question.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Zapis pytania do bazy.
     *
     * @param exam pytanie (konwersja z JSON)
     * @return informacja o wyniku zapisu
     */
    @PostMapping
    public Question createQuestion(@RequestBody Question question) {
        return questionRepository.save(question);
    }


    /**
     * Edycja pytania w bazie.
     *
     * @param id id z URL-a
     * @param updatedQuestion pytanie (konwersja z JSON)
     * @return informacja o wyniku edycji lub wyjątek
     */
    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable UUID id, @Valid @RequestBody Question updatedQuestion) {
        return questionRepository.findById(id)
                .map(existingQuestion -> {
                    existingQuestion.setContent(updatedQuestion.getContent());
                    existingQuestion.setExam(updatedQuestion.getExam());
                    existingQuestion.setAnswers(updatedQuestion.getAnswers()); // jeśli masz listę odpowiedzi
                    existingQuestion.setCorrectAnswer(updatedQuestion.getCorrectAnswer()); // jeśli masz pole poprawnej odpowiedzi
                    Question saved = questionRepository.save(existingQuestion);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Usuwanie pytania w bazy.
     *
     * @param id id z URL-a
     * @return odpowiedź 204 lub 404 gdy pytanie nie istnieje
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
