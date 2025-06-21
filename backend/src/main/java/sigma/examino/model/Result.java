package sigma.examino.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Encja reprezentująca wynik egzaminu uzyskany przez użytkownika.
 * Zawiera odniesienie do użytkownika i egzaminu, uzyskany wynik oraz datę ukończenia.
 */
@Entity
@Table(name = "results")
public class Result {

    /**
     * Unikalny identyfikator wyniku typu UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Użytkownik, który uzyskał ten wynik.
     */
    @ManyToOne
    private User user;

    /**
     * Egzamin, którego dotyczy wynik.
     */
    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    /**
     * Wynik w procentach (np. 85.5).
     */
    private float score;

    /**
     * Data i czas ukończenia egzaminu
     */
    private LocalDateTime timestamp;

    /**
     * Zwraca identyfikator wyniku.
     * @return UUID wyniku
     */
    public UUID getId() {
        return id;
    }

    /**
     * Ustawia identyfikator wyniku.
     * @param id UUID wyniku
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Zwraca użytkownika powiązanego z wynikiem.
     * @return użytkownik
     */
    public User getUser() {
        return user;
    }

    /**
     * Ustawia użytkownika powiązanego z wynikiem.
     * @param user użytkownik
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Zwraca egzamin, którego dotyczy wynik.
     * @return egzamin
     */
    public Exam getExam() {
        return exam;
    }

    /**
     * Ustawia egzamin, którego dotyczy wynik.
     * @param exam egzamin
     */
    public void setExam(Exam exam) {
        this.exam = exam;
    }

    /**
     * Zwraca wynik w procentach.
     * @return wynik egzaminu
     */
    public float getScore() {
        return score;
    }

    /**
     * Ustawia wynik w procentach.
     * @param score wynik egzaminu
     */
    public void setScore(float score) {
        this.score = score;
    }

    /**
     * Zwraca datę i czas ukończenia egzaminu.
     * @return znacznik czasu
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Ustawia datę i czas ukończenia egzaminu.
     * @param timestamp znacznik czasu
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
