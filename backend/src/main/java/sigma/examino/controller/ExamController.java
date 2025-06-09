package sigma.examino.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sigma.examino.model.Exam;
import sigma.examino.repository.ExamRepository;

import java.util.List;
import java.util.UUID;

/**
 * Kontroler REST do zarządzania egzaminami.
 * <p>
 * Obsługuje operacje CRUD (tworzenie, pobieranie, edycję i usuwanie egzaminów).
 * Zabezpieczony adnotacjami Spring Security.
 * </p>
 *
 * @author OpenAI
 * @version 1.0
 */
@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamRepository examRepository;

    /**
     * Zwraca listę wszystkich egzaminów dostępnych w systemie.
     *
     * @return lista egzaminów
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    /**
     * Pobiera egzamin na podstawie identyfikatora.
     *
     * @param id identyfikator egzaminu (UUID)
     * @return obiekt egzaminu
     * @throws RuntimeException jeśli egzamin nie zostanie znaleziony
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Exam getExamById(@PathVariable UUID id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
    }

    /**
     * Tworzy nowy egzamin. Dostępne tylko dla użytkowników z rolą ADMIN.
     *
     * @param exam obiekt egzaminu przesłany w treści żądania
     * @return nowo utworzony egzamin
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Exam> createExam(@Valid @RequestBody Exam exam) {
        if (exam.getQuestionsList() != null) {
            exam.getQuestionsList().forEach(question -> question.setExam(exam));
        }
        Exam savedExam = examRepository.save(exam);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExam);
    }

    /**
     * Aktualizuje istniejący egzamin. Dostępne tylko dla ADMIN.
     *
     * @param id identyfikator egzaminu do aktualizacji
     * @param updatedExam zaktualizowane dane egzaminu
     * @return zaktualizowany egzamin lub 404 jeśli nie znaleziono
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Exam> updateExam(@PathVariable UUID id, @Valid @RequestBody Exam updatedExam) {
        return examRepository.findById(id).map(exam -> {
            exam.setName(updatedExam.getName());
            exam.setDurationMinutes(updatedExam.getDurationMinutes());
            exam.setSubject(updatedExam.getSubject());
            Exam saved = examRepository.save(exam);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Usuwa egzamin na podstawie identyfikatora. Tylko ADMIN.
     *
     * @param id identyfikator egzaminu do usunięcia
     * @return 204 No Content jeśli sukces, 404 jeśli nie znaleziono
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteExam(@PathVariable UUID id) {
        if (!examRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        examRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
