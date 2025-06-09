
import React, { useState } from 'react';
import axios from 'axios';

export default function ExamForm({ onClose, onSaved }) {
  const [exam, setExam] = useState({
    name: '',
    subject: '',
    durationMinutes: 60,
    questionsList: [
      {
        content: '',
        answers: ['', '', '', ''],
        correctAnswerIndex: 0,
      },
    ],
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setExam((prev) => ({ ...prev, [name]: value }));
  };

  const handleQuestionContentChange = (value, qIndex) => {
    const updated = [...exam.questionsList];
    updated[qIndex].content = value;
    setExam({ ...exam, questionsList: updated });
  };

  const handleAnswerChange = (value, qIndex, aIndex) => {
    const updated = [...exam.questionsList];
    updated[qIndex].answers[aIndex] = value;
    setExam({ ...exam, questionsList: updated });
  };

  const handleCorrectAnswerChange = (qIndex, aIndex) => {
    const updated = [...exam.questionsList];
    updated[qIndex].correctAnswerIndex = aIndex;
    setExam({ ...exam, questionsList: updated });
  };

  const addQuestion = () => {
    setExam((prev) => ({
      ...prev,
      questionsList: [
        ...prev.questionsList,
        { content: '', answers: ['', '', '', ''], correctAnswerIndex: 0 },
      ],
    }));
  };

  const removeQuestion = (qIndex) => {
    const updated = [...exam.questionsList];
    updated.splice(qIndex, 1);
    setExam({ ...exam, questionsList: updated });
  };

const handleSubmit = async () => {
  

  console.log("Kliknięto Zapisz");
  const token = localStorage.getItem("token");
  console.log("Token z localStorage:", token);

  if (!token) {
    alert("Musisz być zalogowany jako admin, aby dodać egzamin!");
    return;
  }

  try {
    const response = await axios.post('http://localhost:8080/api/exams', exam, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    console.log('Zapisano egzamin', response.data);
    if (onSaved) onSaved();
  } catch (err) {
    if (err.response) {
      console.error("Błąd odpowiedzi z serwera:", err.response.status, err.response.data);
      if (err.response.status === 403) {
        alert("Brak uprawnień. Zaloguj się jako admin.");
      } else {
        alert("Błąd podczas zapisywania egzaminu: " + JSON.stringify(err.response.data));
      }
    } else {
      console.error("Błąd zapytania:", err.message);
      alert("Błąd podczas zapisywania egzaminu.");
    }
  }
};


  const handleCancel = () => {
    if (onClose) onClose();
  };

  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "#f0f4f8",
        padding: "20px",
      }}
    >
      <div
        style={{
          backgroundColor: "white",
          padding: "30px",
          borderRadius: "8px",
          boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
          maxWidth: "600px",
          width: "100%",
        }}
      >
        <h2 style={{ color: "#333" }}>Dodaj egzamin</h2>

        <input
          placeholder="Nazwa egzaminu"
          name="name"
          value={exam.name}
          onChange={handleChange}
          style={inputStyle}
        />

        <input
          placeholder="Przedmiot"
          name="subject"
          value={exam.subject}
          onChange={handleChange}
          style={inputStyle}
        />

        <label style={{ display: "block", marginBottom: "5px", color: "#555" }}>
          Czas trwania egzaminu (w minutach):
        </label>
        <input
          type="number"
          name="durationMinutes"
          value={exam.durationMinutes}
          onChange={handleChange}
          min="1"
          style={inputStyle}
        />

        <hr style={{ marginBottom: "25px" }} />

        {exam.questionsList.map((question, qIndex) => (
          <div key={qIndex} style={{ marginBottom: "30px" }}>
            <h3 style={{ color: "#444" }}>
              Pytanie {qIndex + 1}
              <button
                onClick={() => removeQuestion(qIndex)}
                style={{
                  marginLeft: "10px",
                  background: "none",
                  border: "none",
                  color: "red",
                  cursor: "pointer",
                  fontSize: "14px",
                }}
              >
                Usuń
              </button>
            </h3>
            <input
              placeholder="Treść pytania"
              value={question.content}
              onChange={(e) => handleQuestionContentChange(e.target.value, qIndex)}
              style={inputStyle}
            />

            {question.answers.map((answer, aIndex) => (
              <div key={aIndex} style={{ display: "flex", alignItems: "center", marginBottom: "8px" }}>
                <input
                  type="radio"
                  name={`correctAnswer-${qIndex}`}
                  checked={question.correctAnswerIndex === aIndex}
                  onChange={() => handleCorrectAnswerChange(qIndex, aIndex)}
                  style={{ marginRight: "10px" }}
                />
                <input
                  placeholder={`Odpowiedź ${aIndex + 1}`}
                  value={answer}
                  onChange={(e) => handleAnswerChange(e.target.value, qIndex, aIndex)}
                  style={{ ...inputStyle, flexGrow: 1 }}
                />
              </div>
            ))}
          </div>
        ))}

        <button onClick={addQuestion} style={blueButton}>
          Dodaj pytanie
        </button>

        <br />
        <button onClick={handleSubmit} style={greenButton}>
          Zapisz
        </button>
        <button onClick={handleCancel} style={grayButton}>
          Anuluj
        </button>
      </div>
    </div>
  );
}

// Style pomocnicze
const inputStyle = {
  width: "100%",
  padding: "10px",
  marginBottom: "15px",
  borderRadius: "4px",
  border: "1px solid #ccc",
};

const blueButton = {
  backgroundColor: "#007bff",
  color: "white",
  padding: "10px 20px",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer",
  marginBottom: "20px",
};

const greenButton = {
  backgroundColor: "#28a745",
  color: "white",
  padding: "10px 20px",
  border: "none",
  borderRadius: "4px",
  cursor: "pointer",
  marginRight: "10px",
};

const grayButton = {
  padding: "10px 20px",
  borderRadius: "4px",
  border: "1px solid #ccc",
  cursor: "pointer",
};
