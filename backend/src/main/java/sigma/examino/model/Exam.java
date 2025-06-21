package sigma.examino.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

/**
 * Encja reprezentująca egzamin w systemie.
 * Zawiera nazwę, czas trwania, przedmiot oraz listę pytań.
 */
@Entity
@Table(name = "exams")
public class Exam {

    /**
     * Unikalny identyfikator egzaminu typu UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Nazwa egzaminu.
     */
    private String name;

    /**
     * Czas trwania egzaminu w minutach.
     */
    private int durationMinutes;

    /**
     * Przedmiot, którego dotyczy egzamin.
     */
    private String subject;

    /**
     * Lista pytań przypisanych do egzaminu.
     */
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Question> questionsList;

    /**
     * Zwraca identyfikator egzaminu.
     * @return UUID egzaminu
     */
    public UUID getId() {
        return id;
    }

    /**
     * Ustawia identyfikator egzaminu.
     * @param id UUID egzaminu
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Zwraca nazwę egzaminu.
     * @return nazwa egzaminu
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia nazwę egzaminu.
     * @param name nazwa egzaminu
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca czas trwania egzaminu w minutach.
     * @return czas trwania
     */
    public int getDurationMinutes() {
        return durationMinutes;
    }

    /**
     * Ustawia czas trwania egzaminu w minutach.
     * @param durationMinutes czas trwania
     */
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    /**
     * Zwraca przedmiot egzaminu.
     * @return przedmiot
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Ustawia przedmiot egzaminu.
     * @param subject przedmiot
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Zwraca listę pytań przypisanych do egzaminu.
     * @return lista pytań
     */
    public List<Question> getQuestionsList() {
        return questionsList;
    }

    /**
     * Ustawia listę pytań przypisanych do egzaminu.
     * @param questionsList lista pytań
     */
    public void setQuestionsList(List<Question> questionsList) {
        this.questionsList = questionsList;
    }
}
