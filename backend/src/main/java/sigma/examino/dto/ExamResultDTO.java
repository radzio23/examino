package sigma.examino.dto;

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
