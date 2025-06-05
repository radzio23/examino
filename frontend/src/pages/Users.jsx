import { useEffect, useState } from "react";
import Menu from "./Menu";

export default function Users() {
  const [students, setStudents] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/auth/students")
      .then((res) => {
        console.log("Odpowiedź z backendu:", res); // <--- DODANE
        if (!res.ok) throw new Error("Błąd sieci");
        return res.json();
      })
      .then((data) => {
        console.log("Studenci:", data); // <--- DODANE
        setStudents(data);
      })
      .catch((err) => {
        console.error("Błąd podczas pobierania studentów:", err);
      });
  }, []);

  return (
    <div className="users-page">
      <Menu />
      <div className="student-list">
        <h2>Lista studentów</h2>
        <ul>
          {students.map((user) => (
            <li key={user.id}>
              {user.username} ({user.role})
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}
