import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../css/ExamForm.scss';

export default function ExamForm({ initialData, onClose, onSaved }) {
  const [exam, setExam] = useState({
    name: '',
    subject: '',
    durationMinutes: 60,
    questionsList: [
      {
        content: '',
        answers: ['', '', '', ''],
        correctAnswer: 0,
      },
    ],
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (initialData) {
      setExam({
        name: initialData.name || '',
        subject: initialData.subject || '',
        durationMinutes: initialData.durationMinutes || 60,
        questionsList: initialData.questionsList.map(q => ({
          content: q.content || '',
          answers: Array.isArray(q.answers) && q.answers.length === 4 ? q.answers : ['', '', '', ''],
          correctAnswer: q.correctAnswer !== undefined ? Number(q.correctAnswer) : 0,
        })),
      });
    }
  }, [initialData?.id]);

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

    const hasErrors =
      Object.keys(newErrors).length > 0 &&
      (newErrors.name || newErrors.subject || newErrors.durationMinutes ||
       newErrors.questionsList.some(q => q.content || q.answers.some(a => a)));

    return !hasErrors;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setExam((prev) => ({ ...prev, [name]: value }));
  };

  const handleQuestionContentChange = (value, qIndex) => {
    setExam(prev => {
      const updated = [...prev.questionsList];
      updated[qIndex].content = value;
      return { ...prev, questionsList: updated };
    });
  };

  const handleAnswerChange = (value, qIndex, aIndex) => {
    setExam(prev => {
      const updated = [...prev.questionsList];
      updated[qIndex].answers[aIndex] = value;
      return { ...prev, questionsList: updated };
    });
  };

  const handleCorrectAnswerChange = (qIndex, aIndex) => {
    setExam(prev => {
      const updated = [...prev.questionsList];
      updated[qIndex].correctAnswer = aIndex;
      return { ...prev, questionsList: updated };
    });
  };

  const addQuestion = () => {
    setExam(prev => ({
      ...prev,
      questionsList: [
        ...prev.questionsList,
        { content: '', answers: ['', '', '', ''], correctAnswer: 0 },
      ],
    }));
  };

  const removeQuestion = (qIndex) => {
    setExam(prev => {
      const updated = [...prev.questionsList];
      updated.splice(qIndex, 1);
      return { ...prev, questionsList: updated };
    });
  };

  const handleSubmit = async () => {
    if (!validate()) return;

    const token = localStorage.getItem('token');
    if (!token) {
      alert('Musisz być zalogowany jako admin, aby wykonać tę operację!');
      return;
    }

    try {
      if (initialData && initialData.id) {
        await axios.put(`http://localhost:8080/api/exams/${initialData.id}`, exam, {
          headers: { Authorization: `Bearer ${token}` },
        });
      } else {
        await axios.post('http://localhost:8080/api/exams', exam, {
          headers: { Authorization: `Bearer ${token}` },
        });
      }
      if (onSaved) onSaved();
    } catch (err) {
      alert('Błąd podczas zapisywania egzaminu.');
      console.error(err);
    }
  };

  return (
    <div className="examFormContainer">
      <div className="examFormBox">
        <h2>{initialData ? 'Edytuj egzamin' : 'Dodaj egzamin'}</h2>

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
              className={`examInput ${errors.questionsList?.[qIndex]?.content ? 'inputError' : ''}`}
            />

            {question.answers.map((answer, aIndex) => (
              <div key={aIndex} className="examAnswerRow">
                <input
                  type="radio"
                  name={`correctAnswer-${qIndex}`}
                  checked={question.correctAnswer === aIndex}
                  onChange={() => handleCorrectAnswerChange(qIndex, aIndex)}
                  className="examRadio"
                />
                <input
                  placeholder={`Odpowiedź ${aIndex + 1}`}
                  value={answer}
                  onChange={(e) => handleAnswerChange(e.target.value, qIndex, aIndex)}
                  className="examInput examAnswerInput"
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
