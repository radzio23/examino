package sigma.examino.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "results")
public class Result {
    @Id
    @GeneratedValue
    // Unikalny identyfikator wyniku typu UUID
    private UUID id;

    // Użytkownik, który uzyskał wynik
    @ManyToOne
    private User user;

    // Egzamin, do którego odnosi się wynik
    @ManyToOne
    private Exam exam;

    // Wynik w procentach (np. 85.5)
    private float score;

    // Data i czas ukończenia egzaminu (ISO 8601)
    private LocalDateTime timestamp;

    // getter id
    public UUID getId() {
        return id;
    }
    // setter id
    public void setId(UUID id) {
        this.id = id;
    }

    // getter użytkownika
    public User getUser() {
        return user;
    }
    // setter użytkownika
    public void setUser(User user) {
        this.user = user;
    }

    // getter egzaminu
    public Exam getExam() {
        return exam;
    }
    // setter egzaminu
    public void setExam(Exam exam) {
        this.exam = exam;
    }

    // getter wyniku w procentach
    public float getScore() {
        return score;
    }
    // setter wyniku w procentach
    public void setScore(float score) {
        this.score = score;
    }

    // getter daty i czasu ukończenia egzaminu
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    // setter daty i czasu ukończenia egzaminu
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
