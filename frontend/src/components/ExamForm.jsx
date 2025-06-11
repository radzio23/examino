import React, { useState } from 'react';
import axios from 'axios';
import '../css/ExamForm.scss';

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

  const [errors, setErrors] = useState({});

  const validate = () => {
    const newErrors = {};
    if (!exam.name.trim()) newErrors.name = true;
    if (!exam.subject.trim()) newErrors.subject = true;
    if (!exam.durationMinutes || exam.durationMinutes < 1) newErrors.durationMinutes = true;

    newErrors.questionsList = exam.questionsList.map((q) => {
      const qErrors = {};
      if (!q.content.trim()) qErrors.content = true;
      qErrors.answers = q.answers.map((ans) => !ans.trim());
      return qErrors;
    });

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0 ||
           !newErrors.questionsList.some(q => q.content || q.answers.some(a => a));
  };

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
    if (!validate()) return;

    const token = localStorage.getItem('token');
    if (!token) {
      alert('Musisz być zalogowany jako admin, aby dodać egzamin!');
      return;
    }

    try {
      await axios.post('http://localhost:8080/api/exams', exam, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (onSaved) onSaved();
    } catch (err) {
      alert('Błąd podczas zapisywania egzaminu.');
    }
  };

  return (
    <div className="examFormContainer">
      <div className="examFormBox">
        <h2 className="examFormTitle">Dodaj egzamin</h2>

        <input
          placeholder="Nazwa egzaminu"
          name="name"
          value={exam.name}
          onChange={handleChange}
          className={`examInput ${errors.name ? 'inputError' : ''}`}
        />

        <input
          placeholder="Przedmiot"
          name="subject"
          value={exam.subject}
          onChange={handleChange}
          className={`examInput ${errors.subject ? 'inputError' : ''}`}
        />

        <label className="examLabel">Czas trwania egzaminu (w minutach):</label>
        <input
          type="number"
          name="durationMinutes"
          value={exam.durationMinutes}
          onChange={handleChange}
          min="1"
          className={`examInput ${errors.durationMinutes ? 'inputError' : ''}`}
        />

        <hr className="examDivider" />

        {exam.questionsList.map((question, qIndex) => (
          <div key={qIndex} className="examQuestionBlock">
            <h3 className="examQuestionTitle">
              Pytanie {qIndex + 1}
              <button
                onClick={() => removeQuestion(qIndex)}
                className="examRemoveQuestionBtn"
                type="button"
              >
                Usuń
              </button>
            </h3>

            <input
              placeholder="Treść pytania"
              value={question.content}
              onChange={(e) => handleQuestionContentChange(e.target.value, qIndex)}
              className={`examInput ${
                errors.questionsList?.[qIndex]?.content ? 'inputError' : ''
              }`}
            />

            {question.answers.map((answer, aIndex) => (
              <div key={aIndex} className="examAnswerRow">
                <input
                  type="radio"
                  name={`correctAnswer-${qIndex}`}
                  checked={question.correctAnswerIndex === aIndex}
                  onChange={() => handleCorrectAnswerChange(qIndex, aIndex)}
                  className="examRadio"
                />
                <input
                  placeholder={`Odpowiedź ${aIndex + 1}`}
                  value={answer}
                  onChange={(e) => handleAnswerChange(e.target.value, qIndex, aIndex)}
                  className={`examInput examAnswerInput ${
                    errors.questionsList?.[qIndex]?.answers?.[aIndex] ? 'inputError' : ''
                  }`}
                />
              </div>
            ))}
          </div>
        ))}

        <button onClick={addQuestion} className="btn blueBtn" type="button">
          Dodaj pytanie
        </button>

        <br />
        <button onClick={handleSubmit} className="btn greenBtn" type="button">
          Zapisz
        </button>
        <button onClick={onClose} className="btn grayBtn" type="button">
          Anuluj
        </button>
      </div>
    </div>
  );
}