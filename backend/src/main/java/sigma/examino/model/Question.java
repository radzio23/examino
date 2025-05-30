package sigma.examino.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue
    // Unikalny identyfikator pytania typu UUID
    private UUID id;

    // Treść pytania
    private String content;

    // Lista możliwych odpowiedzi
    @ElementCollection
    private List<String> answers;

    // Indeks poprawnej odpowiedzi (np. 0, 1, 2, 3)
    private int correctAnswer;

    // Przedmiot, którego dotyczy pytanie
    private String subject;

    // Egzamin, do którego należy pytanie
    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    // getter id
    public UUID getId() {
        return id;
    }
    // setter id
    public void setId(UUID id) {
        this.id = id;
    }

    // getter treści pytania
    public String getContent() {
        return content;
    }
    // setter treści pytania
    public void setContent(String content) {
        this.content = content;
    }

    // getter listy odpowiedzi
    public List<String> getAnswers() {
        return answers;
    }
    // setter listy odpowiedzi
    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    // getter indeksu poprawnej odpowiedzi
    public int getCorrectAnswer() {
        return correctAnswer;
    }
    // setter indeksu poprawnej odpowiedzi
    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    // getter przedmiotu pytania
    public String getSubject() {
        return subject;
    }
    // setter przedmiotu pytania
    public void setSubject(String subject) {
        this.subject = subject;
    }

    // getter egzaminu, do którego należy pytanie
    public Exam getExam() {
        return exam;
    }
    // setter egzaminu, do którego należy pytanie
    public void setExam(Exam exam) {
        this.exam = exam;
    }
}
