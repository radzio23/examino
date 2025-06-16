package sigma.examino.dto;

import java.util.List;
import java.util.UUID;

/**
 * DTO reprezentujące szczegóły pojedynczego pytania w wyniku egzaminu.
 */
public class ExamResultDetailDTO {
    /**
     * Unikalny identyfikator pytania.
     */
    private UUID questionId;

    /**
     * Treść pytania.
     */
    private String questionContent;

    /**
     * Lista możliwych odpowiedzi.
     */
    private List<String> answers;

    /**
     * Indeks poprawnej odpowiedzi.
     */
    private int correctAnswer;

    /**
     * Odpowiedź udzielona przez użytkownika (indeks).
     */
    private Integer userAnswer;

    /**
     * Flaga wskazująca, czy odpowiedź użytkownika jest poprawna.
     */
    private boolean isCorrect;

    /**
     * Pobiera identyfikator pytania.
     * @return UUID pytania
     */
    public UUID getQuestionId() { return questionId; }

    /**
     * Ustawia identyfikator pytania.
     * @param questionId UUID pytania
     */
    public void setQuestionId(UUID questionId) { this.questionId = questionId; }

    /**
     * Pobiera treść pytania.
     * @return treść pytania
     */
    public String getQuestionContent() { return questionContent; }

    /**
     * Ustawia treść pytania.
     * @param questionContent treść pytania
     */
    public void setQuestionContent(String questionContent) { this.questionContent = questionContent; }

    /**
     * Pobiera listę możliwych odpowiedzi.
     * @return lista odpowiedzi
     */
    public List<String> getAnswers() { return answers; }

    /**
     * Ustawia listę możliwych odpowiedzi.
     * @param answers lista odpowiedzi
     */
    public void setAnswers(List<String> answers) { this.answers = answers; }

    /**
     * Pobiera indeks poprawnej odpowiedzi.
     * @return indeks poprawnej odpowiedzi
     */
    public int getCorrectAnswer() { return correctAnswer; }

    /**
     * Ustawia indeks poprawnej odpowiedzi.
     * @param correctAnswer indeks poprawnej odpowiedzi
     */
    public void setCorrectAnswer(int correctAnswer) { this.correctAnswer = correctAnswer; }

    /**
     * Pobiera odpowiedź użytkownika.
     * @return indeks odpowiedzi użytkownika lub null jeśli brak odpowiedzi
     */
    public Integer getUserAnswer() { return userAnswer; }

    /**
     * Ustawia odpowiedź użytkownika.
     * @param userAnswer indeks odpowiedzi użytkownika
     */
    public void setUserAnswer(Integer userAnswer) { this.userAnswer = userAnswer; }

    /**
     * Sprawdza, czy odpowiedź użytkownika jest poprawna.
     * @return true jeśli poprawna, false w przeciwnym razie
     */
    public boolean isCorrect() { return isCorrect; }

    /**
     * Ustawia flagę poprawności odpowiedzi użytkownika.
     * @param correct true jeśli odpowiedź jest poprawna, false jeśli nie
     */
    public void setCorrect(boolean correct) { isCorrect = correct; }
}
