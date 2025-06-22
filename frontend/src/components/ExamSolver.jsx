import React, { useEffect, useState, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import AlertModal from "./Alert";
import "../css/ExamSolver.scss";

/**
 * Komponent do rozwiązywania egzaminu przez studenta.
 * Pobiera dane egzaminu, zarządza odliczaniem czasu oraz przesyłaniem odpowiedzi.
 * Wyświetla formularz pytań lub wynik po zakończeniu.
 */
export default function ExamSolver() {
  const { examId } = useParams();
  const navigate = useNavigate();

  /** Rola użytkownika (domyślnie "STUDENT") */
  const [role, setRole] = useState(localStorage.getItem("role") || "STUDENT");
  /** Dane pobranego egzaminu */
  const [exam, setExam] = useState(null);
  /** Odpowiedzi użytkownika: klucz - id pytania, wartość - indeks odpowiedzi jako string */
  const [answers, setAnswers] = useState({});
  /** Flaga ładowania egzaminu */
  const [loading, setLoading] = useState(true);
  /** Komunikat błędu pobierania egzaminu */
  const [error, setError] = useState(null);
  /** Wynik egzaminu po przesłaniu odpowiedzi */
  const [submitResult, setSubmitResult] = useState(null);
  /** Flaga pokazująca modal potwierdzenia wysłania odpowiedzi */
  const [showConfirm, setShowConfirm] = useState(false);
  /** Pozostały czas egzaminu w sekundach */
  const [timeLeft, setTimeLeft] = useState(null);
  /** Referencja do timera odliczającego czas */
  const timerRef = useRef(null);

  /**
   * Efekt pobierający dane egzaminu po załadowaniu komponentu lub zmianie `examId` / `role`.
   * Sprawdza, czy użytkownik jest studentem, w przeciwnym wypadku przekierowuje.
   * Ustawia czas egzaminu na podstawie `durationMinutes`.
   */
  useEffect(() => {
    if (role !== "STUDENT") {
      alert("Tylko studenci mogą rozwiązywać egzaminy.");
      navigate("/exams");
      return;
    }

    const token = localStorage.getItem("token");
    fetch(`http://localhost:8080/api/exams/${examId}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
        .then((res) => {
          if (!res.ok) throw new Error("Nie udało się pobrać egzaminu");
          return res.json();
        })
        .then((data) => {
          setExam(data);
          setLoading(false);
          setTimeLeft(data.durationMinutes * 60);
        })
        .catch((err) => {
          setError(err.message);
          setLoading(false);
        });
  }, [examId, role, navigate]);

  /**
   * Efekt odliczający czas egzaminu co sekundę.
   * Jeśli czas dobiegnie końca, wymusza automatyczne przesłanie odpowiedzi.
   */
  useEffect(() => {
    if (timeLeft === null || submitResult) return;

    if (timeLeft <= 0) {
      handleSubmit(true);
      return;
    }

    timerRef.current = setTimeout(() => {
      setTimeLeft(timeLeft - 1);
    }, 1000);

    return () => clearTimeout(timerRef.current);
  }, [timeLeft, submitResult]);

  /**
   * Aktualizuje odpowiedź użytkownika na dane pytanie.
   * @param {string} questionId - Id pytania.
   * @param {number} answerIndex - Indeks wybranej odpowiedzi.
   */
  const handleAnswerChange = (questionId, answerIndex) => {
    setAnswers((prev) => ({
      ...prev,
      [questionId]: answerIndex.toString(),
    }));
  };

  /**
   * Przesyła odpowiedzi użytkownika na serwer.
   * Waliduje kompletność odpowiedzi, jeśli nie jest to wymuszone przesłanie.
   * @param {boolean} [submitForced=false] - Czy przesłanie jest wymuszone (np. po upływie czasu).
   */
  const handleSubmit = (submitForced = false) => {
    if (!submitForced && !exam.questionsList.every((q) => answers[q.id] !== undefined)) {
      alert("Proszę odpowiedzieć na wszystkie pytania");
      return;
    }

    setShowConfirm(false);

    const token = localStorage.getItem("token");
    const payload = {
      examId: exam.id,
      answers: answers,
    };

    fetch("http://localhost:8080/api/submit", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    })
        .then((res) => {
          if (!res.ok) throw new Error("Błąd podczas przesyłania odpowiedzi");
          return res.json();
        })
        .then((data) => {
          setSubmitResult(data);
        })
        .catch((err) => {
          alert(err.message);
        });
  };

  /**
   * Formatuje czas w sekundach do postaci MM:SS.
   * @param {number} seconds - Czas w sekundach.
   * @returns {string} Sformatowany czas.
   */
  const formatTime = (seconds) => {
    const m = Math.floor(seconds / 60)
        .toString()
        .padStart(2, "0");
    const s = (seconds % 60).toString().padStart(2, "0");
    return `${m}:${s}`;
  };

  if (loading) return <div className="examSolverPage">Ładowanie egzaminu...</div>;
  if (error) return <div className="examSolverPage">Błąd: {error}</div>;

  return (
      <div className={`examSolverPage ${submitResult ? "resultPage" : ""}`}>
        <div className="examSolverBox">
          {submitResult ? (
              <>
                <h2 className="exam-title">Wynik egzaminu</h2>
                <p>
                  Wynik: {submitResult.score.toFixed(2)}% ({submitResult.correctCount}/{submitResult.total})
                </p>
                <h3 className="questions-header">Pytania:</h3>
                <ul className="questions-list">
                  {submitResult.details.map((detail) => {
                    const userAnswerIndex = detail.userAnswer;
                    const correctAnswerIndex = detail.correctAnswer;
                    const userIsCorrect = detail.correct;
                    const userAnswered = userAnswerIndex !== undefined && userAnswerIndex !== null;

                    return (
                        <li key={detail.questionId} className="question-item">
                          <div className="question-content">
                            {detail.questionContent}
                            <span>{userIsCorrect ? "1/1" : "0/1"}</span>
                          </div>

                          <ul className="answers-list">
                            {detail.answers.map((answerText, idx) => {
                              const isUserAnswer = idx === userAnswerIndex;
                              let className = "answer";

                              if (userAnswered) {
                                if (isUserAnswer && userIsCorrect) className += " user-correct";
                                else if (isUserAnswer && !userIsCorrect) className += " user-wrong";
                              }

                              return (
                                  <li key={idx} className={className}>
                                    {answerText}
                                  </li>
                              );
                            })}
                          </ul>

                          {userAnswered ? (
                              userIsCorrect ? (
                                  <div className="user-answer-feedback correct-feedback">
                                    Twoja odpowiedź jest poprawna
                                  </div>
                              ) : (
                                  <div className="user-answer-feedback wrong-feedback">
                                    Twoja odpowiedź jest błędna. Poprawna odpowiedź to:{" "}
                                    <strong>{detail.answers[correctAnswerIndex]}</strong>
                                  </div>
                              )
                          ) : (
                              <div className="user-answer-feedback no-answer-feedback">
                                Nie zaznaczyłeś żadnej odpowiedzi. Poprawna odpowiedź to:{" "}
                                <strong>{detail.answers[correctAnswerIndex]}</strong>
                              </div>
                          )}
                        </li>
                    );
                  })}
                </ul>

                <button onClick={() => navigate("/exams")} className="exit-button">
                  Powrót do listy egzaminów
                </button>
              </>
          ) : (
              <>
                <div className="examHeader">
                  <h1>{exam.name}</h1>
                  <p>
                    {exam.subject} — czas trwania: {exam.durationMinutes} minut
                  </p>
                  <p style={{ fontWeight: "600", fontSize: "1.2rem", color: "#00674F" }}>
                    Pozostały czas: {formatTime(timeLeft)}
                  </p>
                </div>

                {exam.questionsList.map((q, i) => (
                    <div key={q.id} className="questionBlock">
                      <h3>Pytanie {i + 1}:</h3>
                      <p>{q.content}</p>
                      <div className="answers">
                        {q.answers.map((answerText, idx) => (
                            <label key={idx} className="answerOption">
                              <input
                                  type="radio"
                                  name={`question-${q.id}`}
                                  value={idx}
                                  checked={answers[q.id] === idx.toString()}
                                  onChange={() => handleAnswerChange(q.id, idx)}
                              />
                              {answerText}
                            </label>
                        ))}
                      </div>
                    </div>
                ))}

                <button onClick={() => setShowConfirm(true)} className="submitBtn">
                  Wyślij odpowiedzi
                </button>

                {showConfirm && (
                    <AlertModal
                        message="Czy na pewno chcesz zakończyć egzamin i wysłać odpowiedzi?"
                        onClose={() => setShowConfirm(false)}
                        onConfirm={() => handleSubmit(false)}
                    />
                )}
              </>
          )}
        </div>
      </div>
  );
}
