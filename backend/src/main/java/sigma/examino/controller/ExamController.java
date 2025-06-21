package sigma.examino.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sigma.examino.model.Exam;
import sigma.examino.model.User;
import sigma.examino.repository.ExamRepository;
import sigma.examino.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Zwraca listę wszystkich nierozwiązanych egzaminów (widoczne dla zalogowanych użytkowników).
     */
    @GetMapping()
//    @PreAuthorize("isAuthenticated()")
    public List<Exam> getAllExams(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Użytkownik nie istnieje"));
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + examRepository.findExamsNotTakenByUser(user.getId()));

        return examRepository.findExamsNotTakenByUser(user.getId());
    }

    /**
     * Zwraca konkretny egzamin (widoczny dla zalogowanych).
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Exam getExamById(@PathVariable UUID id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
    }



    /**
     * Tworzy egzamin – tylko ADMIN.
     */
    @PostMapping
    //@PreAuthorize("isAuthenticated()")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Exam> createExam(@Valid @RequestBody Exam exam) {
        if (exam.getQuestionsList() != null) {
            exam.getQuestionsList().forEach(question -> question.setExam(exam));
        }
        try
        {
            Exam savedExam = examRepository.save(exam);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedExam);
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    /**
     * Edytuje egzamin – tylko ADMIN.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Exam> updateExam(@PathVariable UUID id, @Valid @RequestBody Exam updatedExam) {
        return examRepository.findById(id).map(exam -> {
            exam.setName(updatedExam.getName());
            exam.setDurationMinutes(updatedExam.getDurationMinutes());
            exam.setSubject(updatedExam.getSubject());


            if (updatedExam.getQuestionsList() != null) {
                updatedExam.getQuestionsList().forEach(q -> q.setExam(exam));
                exam.getQuestionsList().clear();
                exam.getQuestionsList().addAll(updatedExam.getQuestionsList());
            }

            Exam saved = examRepository.save(exam);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    /**
     * Usuwa egzamin – tylko ADMIN.
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