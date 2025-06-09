import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Menu from "./Menu";
import '../css/Exam.css';
import { getUserRole } from "../utils/auth";

export default function Exam() {
  const [exams, setExams] = useState([]);
  const role = getUserRole(); // odczytuje rolę z tokena
  console.log("Rola użytkownika:", role);

  useEffect(() => {
    const token = localStorage.getItem("token");
    fetch("http://localhost:8080/api/exams", {
      headers: {
        Authorization: `Bearer ${token}`, // dodajemy token w nagłówku
      },
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error(`HTTP error! status: ${res.status}`);
        }
        return res.json();
      })
      .then((data) => setExams(data))
      .catch((err) => console.error("Błąd podczas pobierania egzaminów:", err));
  }, []);

  return (
    <div className="exam">
      <Menu />
      <div className="exam-list">
        <h2>Egzaminy:</h2>
        <ul>
          {exams.map((exam) => (
            <li key={exam.id}>
              {exam.name} - {exam.subject}
            </li>
          ))}
        </ul>
        {/* Przycisk "Dodaj egzamin" widoczny tylko dla ADMIN */}
        {role === "ADMIN" && (
          <Link to="/exams/new">
            <button>Dodaj egzamin</button>
          </Link>
        )}
      </div>
    </div>
  );
}
