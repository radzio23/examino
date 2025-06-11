import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Menu from "../components/Menu";
import '../css/Exam.scss';
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
    <div>
      <Menu/>
        <div className="exams">
            <div className={"examsMenu"}>
                <h1>Twoje egzaminy:</h1>
                {role === "ADMIN" && (<Link to="/exams/new"><button>Dodaj egzamin</button></Link>)}
            </div>
            <div className={"examsList"}>
                {exams.map((exam) => (
                    <div className={"exam"}>
                        <div className={"top"}>
                            <h2 key={exam.id}>{exam.name}</h2>
                            <img alt="edit" src={"/images/edit.png"}/>
                            <img alt="delete" src={"/images/delete.png"}/>
                        </div>
                        <p>{exam.subject} - {exam.durationMinutes} minut</p>
                    </div>
                ))}
            </div>
        </div>
    </div>
  );
}
