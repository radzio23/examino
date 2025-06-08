import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Menu from "./Menu";
import '../css/Exam.css';


export default function Exam() {
  const [exams, setExams] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/exams")
      .then((res) => res.json())
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

        {/* Przycisk przekierowujący do formularza */}
        <Link to="/exams/new">
          <button>Dodaj egzamin</button>
        </Link>
      </div>
    </div>
  );
}
