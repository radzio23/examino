package sigma.examino.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * Zapis egzaminu do bazy.
     *
     * @param exam egzamin (konwersja z JSON)
     * @return informacja o wyniku zapisu
     */
    @PostMapping
    public Exam createExam(@RequestBody Exam exam) {
        return examRepository.save(exam);
    }

    /**
     * Edycja egzaminu w bazie.
     *
     * @param id id z URL-a
     * @param updatedExam egzamin (konwersja z JSON)
     * @return informacja o wyniku edycji lub wyjątek
     */
    @PutMapping("/{id}")
    public Exam updateExam(@PathVariable UUID id, @RequestBody Exam updatedExam) {
        return examRepository.findById(id).map(exam -> {
                    exam.setName(updatedExam.getName());
                    exam.setDurationMinutes(updatedExam.getDurationMinutes());
                    exam.setSubject(updatedExam.getSubject());
                    exam.setQuestionsList(updatedExam.getQuestionsList());
                    return examRepository.save(exam);
                }).orElseThrow(() -> new RuntimeException("Exam not found"));
    }

    /**
     * Usuwanie egzaminu w bazy.
     *
     * @param id id z URL-a
     */
    @DeleteMapping("/{id}")
    public void deleteExam(@PathVariable UUID id) {
        examRepository.deleteById(id);
    }
}