package sigma.examino.controller;
import jakarta.validation.Valid; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sigma.examino.model.Exam;
import sigma.examino.repository.ExamRepository;

import java.util.List;
import java.util.UUID;

/**
 * Kontroler obsługujący endpointy związane z egzaminami - dodawaniem, usuwaniem, edytowaniem i wyświetlaniem.
 */
@RestController
@RequestMapping("/api/exams")
public class ExamController {
    @Autowired
    private ExamRepository examRepository;

    /**
     * Zwraca listę wszystkich egzaminów.
     *
     * @return lista egzaminów
     */
    @GetMapping // odpowiada na GET /api/exams
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    /**
     * Zwraca egzamin o podanym id.
     *
     * @param id id z URL-a
     * @return egzamin o danym id lub wyjątek
     */
    @GetMapping("/{id}")
    public Exam getExamById(@PathVariable UUID id){
        return examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
    }
    //===0806=======
    @PostMapping
    public ResponseEntity<Exam> createExam(@Valid @RequestBody Exam exam) {
        // Powiąż pytania z egzaminem
        if (exam.getQuestionsList() != null) {
            exam.getQuestionsList().forEach(question -> question.setExam(exam));
        }
        Exam savedExam = examRepository.save(exam);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExam);
    }



        //TO  DODAJE 0506
    /**
     * Edycja egzaminu w bazie z walidacją.
     *
     * @param id id z URL-a
     * @param updatedExam egzamin (konwersja z JSON)
     * @return HTTP 200 OK z zaktualizowanym egzaminem lub 404 jeśli nie znaleziono
     */
    @PutMapping("/{id}")
    public ResponseEntity<Exam> updateExam(@PathVariable UUID id, @Valid @RequestBody Exam updatedExam) {
        return examRepository.findById(id).map(exam -> {
                    exam.setName(updatedExam.getName());
                    exam.setDurationMinutes(updatedExam.getDurationMinutes());
                    exam.setSubject(updatedExam.getSubject());
                    // Lepiej nie ustawiać tutaj listy pytań, bo pytania usuwamy osobmno
                    // exam.setQuestionsList(updatedExam.getQuestionsList());
                    Exam saved = examRepository.save(exam);
                    return ResponseEntity.ok(saved);
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Usuwanie egzaminu z bazy.
     *
     * @param id id z URL-a
     * @return HTTP 204 No Content jeśli usunięto, 404 jeśli nie znaleziono
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable UUID id) {
        if (!examRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        examRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}