package sigma.examino.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    // Unikalny identyfikator egzaminu typu UUID
    private UUID id;

    // Nazwa egzaminu
    private String name;

    // Czas trwania egzaminu w minutach
    private int durationMinutes;

    // Przedmiot, którego dotyczy egzamin
    private String subject;

    // Lista pytań przypisanych do egzaminu
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Question> questionsList;

    // getter id
    public UUID getId() {
        return id;
    }
    // setter id
    public void setId(UUID id) {
        this.id = id;
    }

    // getter nazwy
    public String getName() {
        return name;
    }
    // setter nazwy
    public void setName(String name) {
        this.name = name;
    }

    // getter czasu
    public int getDurationMinutes() {
        return durationMinutes;
    }
    // setter czasu
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    // getter przedmiotu
    public String getSubject() {
        return subject;
    }
    // setter przedmiotu
    public void setSubject(String subject) {
        this.subject = subject;
    }

    // getter listy pytań
    public List<Question> getQuestionsList() {
        return questionsList;
    }
    // setter listy pytań
    public void setQuestionsList(List<Question> questionsList) {
        this.questionsList = questionsList;
    }
}
