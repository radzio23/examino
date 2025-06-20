package sigma.examino.dto;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO reprezentujące wynik egzaminu, wraz z podsumowaniem i szczegółami pytań.
 */
public class ExamResultDTO {
    /**
     * Wynik procentowy egzaminu.
     */
    private float score;

    /**
     * Czas wykonania egzaminu.
     */
    private LocalDateTime timestamp;

    /**
     * Nazwa egzaminu.
     */
    private String examName;

    /**
     * Nazwa egzaminu.
     */
    private String subject;

    /**
     * Liczba poprawnych odpowiedzi.
     */
    private int correctCount;

    /**
     * Całkowita liczba pytań na egzaminie.
     */
    private int total;

    /**
     * Lista szczegółów dotyczących poszczególnych pytań i odpowiedzi.
     */
    private List<ExamResultDetailDTO> details;

    /**
     * Pobiera wynik egzaminu w procentach.
     * @return wynik procentowy
     */
    public float getScore() {
        return score;
    }

    /**
     * Ustawia wynik egzaminu w procentach.
     * @param score wynik procentowy
     */
    public void setScore(float score) {
        this.score = score;
    }

    /**
     * Pobiera czas wykonania egzaminu.
     * @return czas wykonania egzaminu
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Ustawia czas wykonania egzaminu.
     * @param timestamp czas wykonania egzaminu
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Pobiera nazwe egzaminu.
     * @return nazwa egzaminu
     */
    public String getExamName() {
        return examName;
    }

    /**
     * Ustawia nazwe egzaminu.
     * @param examName nazwa egzaminu
     */
    public void setExamName(String examName) {
        this.examName = examName;
    }

    /**
     * Pobiera nazwe przedmiotu.
     * @return nazwa przedmiotu
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Ustawia przedmiot.
     * @param subject nazwa przedmiotu
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Pobiera liczbę poprawnych odpowiedzi.
     * @return liczba poprawnych odpowiedzi
     */
    public int getCorrectCount() {
        return correctCount;
    }

    /**
     * Ustawia liczbę poprawnych odpowiedzi.
     * @param correctCount liczba poprawnych odpowiedzi
     */
    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    /**
     * Pobiera całkowitą liczbę pytań na egzaminie.
     * @return liczba pytań
     */
    public int getTotal() {
        return total;
    }

    /**
     * Ustawia całkowitą liczbę pytań na egzaminie.
     * @param total liczba pytań
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * Pobiera listę szczegółów dotyczących pytań i odpowiedzi.
     * @return lista szczegółów
     */
    public List<ExamResultDetailDTO> getDetails() {
        return details;
    }

    /**
     * Ustawia listę szczegółów dotyczących pytań i odpowiedzi.
     * @param details lista szczegółów
     */
    public void setDetails(List<ExamResultDetailDTO> details) {
        this.details = details;
    }
}
