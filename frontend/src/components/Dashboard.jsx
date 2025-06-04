import { useEffect, useState } from "react";
import ExamList from "../components/ExamList";

export default function Dashboard() {
  const [exams, setExams] = useState([]);

  // Pobierz dane z backendu (Spring Boot na localhost:8080)
  useEffect(() => {
    fetch("http://localhost:8080/api/exams")
      .then((res) => res.json())
      .then((data) => setExams(data.slice(0, 5))); // tylko 5 egzaminów
  }, []);

  return (
    <div className="dashboard">
      <h1>Witaj w panelu administracyjnym</h1>
      <ExamList exams={exams} />
    </div>
  );
}