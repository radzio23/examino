package sigma.examino.dto;

import java.util.Map;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) reprezentujący przesłanie odpowiedzi na egzamin.
 * Zawiera identyfikator egzaminu oraz mapę odpowiedzi, gdzie kluczem jest
 * identyfikator pytania, a wartością odpowiedź użytkownika.
 */
public class ExamSubmissionDTO {

    /**
     * Identyfikator egzaminu, który użytkownik rozwiązuje.
     */
    private UUID examId;

    /**
     * Mapa odpowiedzi użytkownika na pytania.
     * Klucz: UUID pytania.
     * Wartość: String z odpowiedzią
     */
    private Map<String, String> answers;

    /**
     * Pobiera identyfikator egzaminu.
     *
     * @return UUID egzaminu
     */
    public UUID getExamId() {
        return examId;
    }

    /**
     * Ustawia identyfikator egzaminu.
     *
     * @param examId UUID egzaminu do ustawienia
     */
    public void setExamId(UUID examId) {
        this.examId = examId;
    }

    /**
     * Pobiera mapę odpowiedzi użytkownika na pytania.
     *
     * @return mapa, gdzie klucz to UUID pytania, a wartość to odpowiedź użytkownika
     */
    public Map<String, String> getAnswers() {
        return answers;
    }

    /**
     * Ustawia mapę odpowiedzi użytkownika na pytania.
     *
     * @param answers mapa odpowiedzi, gdzie klucz to UUID pytania,
     *                a wartość to odpowiedź użytkownika
     */
    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }
}
