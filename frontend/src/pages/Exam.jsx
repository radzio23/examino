import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Menu from "../components/Menu";
import '../css/Exam.scss';
import { getUserRole } from "../utils/auth";
import AlertModal from "../components/Alert";
import ExamForm from "../components/ExamForm";

export default function Exam() {
  const [exams, setExams] = useState([]);
  const [showAlert, setShowAlert] = useState(false);
  const [selectedExamId, setSelectedExamId] = useState(null);
  const [editingExam, setEditingExam] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const role = getUserRole();

  useEffect(() => {
    fetchExams();
  }, []);

  const fetchExams = () => {
    const token = localStorage.getItem("token");
    fetch("http://localhost:8080/api/exams", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
        return res.json();
      })
      .then((data) => setExams(data))
      .catch((err) => console.error("Błąd podczas pobierania egzaminów:", err));
  };

  const deleteExam = (id) => {
    const token = localStorage.getItem("token");
    fetch(`http://localhost:8080/api/exams/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        if (!res.ok) throw new Error("Błąd usuwania egzaminu");
        setExams((prev) => prev.filter((exam) => exam.id !== id));
        setShowAlert(false);
      })
      .catch((err) => {
        console.error("Błąd usuwania:", err);
        setShowAlert(false);
      });
  };

  const confirmDelete = () => {
    if (selectedExamId) {
      deleteExam(selectedExamId);
    }
  };

  const handleEditClick = (exam) => {
    setEditingExam(exam);
    setShowForm(true);
  };

  const handleAddClick = () => {
    setEditingExam(null);
    setShowForm(true);
  };

  const handleCloseForm = () => {
    setShowForm(false);
    setEditingExam(null);
  };

  const handleSaved = () => {
    fetchExams();
    handleCloseForm();
  };

  if (showForm) {
    return (
      <div>
        <Menu />
        <ExamForm
          key={editingExam?.id || 'new'}
          initialData={editingExam}
          onSaved={handleSaved}
          onClose={handleCloseForm}
        />
      </div>
    );
  }

  return (
    <div>
      <Menu />
      <div className="exams">
        <div className="examsMenu">
          <h1>Twoje egzaminy:</h1>
          {role === "ADMIN" && (
            <button onClick={handleAddClick}>Dodaj egzamin</button>
          )}
        </div>

        <div className="examsList">
          {exams.map((exam) => (
            <div className="exam" key={exam.id}>
              <div className="top">
                <Link to={`/exams/${exam.id}`}>
                  <h2>{exam.name}</h2>
                </Link>
                {role === "ADMIN" && (
                  <>
                    <img
                      alt="edit"
                      src="/images/edit.png"
                      onClick={() => handleEditClick(exam)}
                    />
                    <img
                      alt="delete"
                      src="/images/delete.png"
                      onClick={() => {
                        setSelectedExamId(exam.id);
                        setShowAlert(true);
                      }}
                    />
                  </>
                )}
              </div>
              <p>{exam.subject} - {exam.durationMinutes} minut</p>
            </div>
          ))}
        </div>
      </div>

      {showAlert && (
        <AlertModal
          message="Czy na pewno chcesz usunąć ten egzamin?"
          onClose={() => setShowAlert(false)}
          onConfirm={confirmDelete}
        />
      )}
    </div>
  );
}
