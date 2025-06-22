import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { getUserRole } from "../utils/auth";
import '../css/Exam.scss';
import Menu from "../components/Menu";
import AlertModal from "../components/Alert";
import ExamForm from "../components/ExamForm";

/**
 * @component Exam
 * @description
 * Komponent zarządzający listą egzaminów. Umożliwia wyświetlanie, wyszukiwanie,
 * dodawanie, edycję i usuwanie egzaminów w zależności od roli użytkownika.
 */
export default function Exam() {
  /** @state {Array} exams - lista egzaminów pobrana z API */
  const [exams, setExams] = useState([]);

  /** @state {boolean} showAlert - czy wyświetlić modal z potwierdzeniem usunięcia */
  const [showAlert, setShowAlert] = useState(false);

  /** @state {string|null} selectedExamId - id egzaminu wybranego do usunięcia */
  const [selectedExamId, setSelectedExamId] = useState(null);

  /** @state {object|null} editingExam - egzamin aktualnie edytowany (lub null) */
  const [editingExam, setEditingExam] = useState(null);

  /** @state {boolean} showForm - czy pokazać formularz dodawania/edycji egzaminu */
  const [showForm, setShowForm] = useState(false);

  /** @state {string} searchTerm - aktualna fraza wyszukiwania egzaminów */
  const [searchTerm, setSearchTerm] = useState("");

  /** @constant {string} role - rola aktualnego użytkownika (np. ADMIN, STUDENT) */
  const role = getUserRole();

  /**
   * Efekt uruchamiany po zamontowaniu komponentu,
   * pobiera listę egzaminów z serwera.
   */
  useEffect(() => {
    fetchExams();
  }, []);

  /**
   * Funkcja pobierająca egzaminy z API i ustawiająca je w stanie komponentu.
   */
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

  /**
   * Funkcja usuwająca egzamin o podanym id z API i aktualizująca listę egzaminów.
   * @param {string} id - id egzaminu do usunięcia
   */
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

  /**
   * Funkcja potwierdzająca usunięcie wybranego egzaminu.
   */
  const confirmDelete = () => {
    if (selectedExamId) {
      deleteExam(selectedExamId);
    }
  };

  /**
   * Obsługa kliknięcia przycisku edycji egzaminu.
   * @param {object} exam - egzamin do edycji
   * @param {Event} e - event kliknięcia (do zatrzymania propagacji)
   */
  const handleEditClick = (exam, e) => {
    e.stopPropagation();
    setEditingExam(exam);
    setShowForm(true);
  };

  /**
   * Obsługa kliknięcia przycisku dodania nowego egzaminu.
   */
  const handleAddClick = () => {
    setEditingExam(null);
    setShowForm(true);
  };

  /**
   * Zamknięcie formularza dodawania/edycji egzaminu.
   */
  const handleCloseForm = () => {
    setShowForm(false);
    setEditingExam(null);
  };

  /**
   * Obsługa zdarzenia zapisania egzaminu (po dodaniu lub edycji),
   * powoduje odświeżenie listy egzaminów.
   */
  const handleSaved = () => {
    fetchExams();
    handleCloseForm();
  };

  /** @constant {function} navigate - funkcja nawigacji */
  const navigate = useNavigate();

  // Jeśli formularz jest aktywny, wyświetlamy go zamiast listy egzaminów
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

  // Renderowanie listy egzaminów i menu
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
          <hr /><br />

          <div className="examsList">
            {exams
                .filter((exam) =>
                    exam.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                    exam.subject.toLowerCase().includes(searchTerm.toLowerCase())
                )
                .map((exam) => (
                    <div
                        className="exam"
                        key={exam.id}
                        onClick={() => {
                          if (role === 'ADMIN') {
                            navigate(`/exams/${exam.id}`);
                          } else {
                            navigate(`/exams/${exam.id}/solve`);
                          }
                        }}
                    >
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
