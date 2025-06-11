import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import Menu from "../components/Menu";
import '../css/ExamDetails.scss';

export default function ExamDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [exam, setExam] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem("token");
    fetch(`http://localhost:8080/api/exams/${id}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Błąd ładowania egzaminu");
        return res.json();
      })
      .then((data) => setExam(data))
      .catch((err) => console.error(err));
  }, [id]);

  if (!exam) return <p className="loading">Ładowanie egzaminu...</p>;

  return (
    <div>
      <Menu />
      <div className="exam-details">
        <h1 className="exam-title">{exam.name}</h1>
        <p><strong>Przedmiot:</strong> {exam.subject}</p>
        <p><strong>Czas trwania:</strong> {exam.durationMinutes} minut</p>

        {exam.questionsList && exam.questionsList.length > 0 ? (
          <>
            <h3 className="questions-header">Pytania:</h3>
            <ul className="questions-list">
              {exam.questionsList.map((question, idx) => (
                <li key={question.id || idx} className="question-item">
                  <p className="question-content">{question.content}</p>
                  <ul className="answers-list">
                    {question.answers.map((answer, aIdx) => (
                      <li
                        key={aIdx}
                        className={
                          aIdx === question.correctAnswer
                            ? "answer correct"
                            : "answer"
                        }
                      >
                        {answer} {aIdx === question.correctAnswer && <span className="correct-label">✓</span>}
                      </li>
                    ))}
                  </ul>
                </li>
              ))}
            </ul>
          </>
        ) : (
          <p className="no-questions">Brak pytań w tym egzaminie.</p>
        )}

        <button className="exit-button" onClick={() => navigate("/exams")}>
          Zakończ przegląd
        </button>
      </div>
    </div>
  );
}