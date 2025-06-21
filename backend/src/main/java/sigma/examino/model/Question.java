package sigma.examino.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * Encja reprezentująca pojedyncze pytanie egzaminacyjne.
 * Zawiera treść pytania, listę możliwych odpowiedzi, poprawną odpowiedź
 * oraz informację o przypisanym egzaminie i przedmiocie.
 */
@Entity
@Table(name = "questions")
public class Question {

    /**
     * Unikalny identyfikator pytania typu UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Treść pytania.
     */
    private String content;

    /**
     * Lista możliwych odpowiedzi.
     */
    @ElementCollection
    private List<String> answers;

    /**
     * Indeks poprawnej odpowiedzi
     */
    private int correctAnswer;

    /**
     * Przedmiot, którego dotyczy pytanie.
     */
    private String subject;

    /**
     * Egzamin, do którego przypisane jest to pytanie.
     */
    @ManyToOne
    @JoinColumn(name = "exam_id")
    @JsonBackReference
    private Exam exam;

    /**
     * Zwraca identyfikator pytania.
     * @return UUID pytania
     */
    public UUID getId() {
        return id;
    }

    /**
     * Ustawia identyfikator pytania.
     * @param id UUID pytania
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Zwraca treść pytania.
     * @return treść pytania
     */
    public String getContent() {
        return content;
    }

    /**
     * Ustawia treść pytania.
     * @param content treść pytania
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Zwraca listę możliwych odpowiedzi.
     * @return lista odpowiedzi
     */
    public List<String> getAnswers() {
        return answers;
    }

    /**
     * Ustawia listę możliwych odpowiedzi.
     * @param answers lista odpowiedzi
     */
    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    /**
     * Zwraca indeks poprawnej odpowiedzi.
     * @return indeks odpowiedzi
     */
    public int getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Ustawia indeks poprawnej odpowiedzi.
     * @param correctAnswer indeks odpowiedzi
     */
    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * Zwraca nazwę przedmiotu, którego dotyczy pytanie.
     * @return nazwa przedmiotu
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Ustawia nazwę przedmiotu pytania.
     * @param subject nazwa przedmiotu
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Zwraca egzamin, do którego należy pytanie.
     * @return egzamin
     */
    public Exam getExam() {
        return exam;
    }

    /**
     * Ustawia egzamin, do którego należy pytanie.
     * @param exam egzamin
     */
    public void setExam(Exam exam) {
        this.exam = exam;
    }
}
