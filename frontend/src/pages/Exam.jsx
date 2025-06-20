import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { getUserRole } from "../utils/auth";
import '../css/Exam.scss';
import Menu from "../components/Menu";
import AlertModal from "../components/Alert";
import ExamForm from "../components/ExamForm";

export default function Exam() {
  const [exams, setExams] = useState([]);
  const [showAlert, setShowAlert] = useState(false);
  const [selectedExamId, setSelectedExamId] = useState(null);
  const [editingExam, setEditingExam] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
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

  const handleEditClick = (exam, e) => {
    e.stopPropagation();
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

  const navigate = useNavigate();

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
          <input
            type="text"
            placeholder="Szukaj po nazwie lub przedmiocie..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="examSearchInput"
          />
          {role === "ADMIN" && (
            <button onClick={handleAddClick}>Dodaj egzamin</button>
          )}
        </div>
        <hr/><br/>

        <div className="examsList">
          {exams
            .filter((exam) =>
              exam.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
              exam.subject.toLowerCase().includes(searchTerm.toLowerCase())
            )
            .map((exam) => (
              <div className="exam" key={exam.id} onClick={() => {
                if (role === 'ADMIN') {
                  navigate(`/exams/${exam.id}`);
                } else {
                  navigate(`/exams/${exam.id}/solve`);
                }
              }}>
                <div className="top">
                    <h2>{exam.name}</h2>

                  {role === "ADMIN" && (
                    <>
                      <img
                        alt="edit"
                        src="/images/edit.png"
                        onClick={(e) => handleEditClick(exam, e)}
                      />
                      <img
                        alt="delete"
                        src="/images/delete.png"
                        onClick={(e) => {
                          e.stopPropagation();
                          setSelectedExamId(exam.id);
                          setShowAlert(true);
                        }}
                      />
                    </>
                  )}
                </div>
                <p>{exam.subject}</p>
                <p>{exam.durationMinutes} minut</p>
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
