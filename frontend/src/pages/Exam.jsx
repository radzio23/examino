import {useEffect, useState} from "react";
import Menu from "./Menu";

export default function Exam() {
  const [exams, setExams] = useState([]);

  // pobiera listę egzaminów z backendu
  useEffect(() => {
    fetch("http://localhost:8080/api/exams")
      .then((res) => res.json())
      .then((data) => setExams(data));
  }, []);

  return (
    <div className="exam">
      <Menu/>
      <div className="exam-list">
        <h2>Egzaminy:</h2>
        <ul>
          {exams.map((exam) => (
            <li key={exam.id}>{exam.name} - {exam.subject}</li>
          ))}
        </ul>
      </div>
    </div>
  );
}