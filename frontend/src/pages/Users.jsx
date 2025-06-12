import { useEffect, useState } from "react";
import Menu from "../components/Menu";
import '../css/Users.scss';
import AlertModal from "../components/Alert";

export default function Users() {
  const [students, setStudents] = useState([]);
  const [showAlert, setShowAlert] = useState(false);
  const [selectedStudentId, setSelectedStudentId] = useState(null);

  const deleteUser = (id) => {
      const token = localStorage.getItem("token");
      fetch(`http://localhost:8080/api/auth/${id}`, {
          method: "DELETE",
          headers: {
              Authorization: `Bearer ${token}`,
          },
      })
          .then((res) => {
              if (!res.ok) throw new Error("Błąd usuwania studenta");
              setStudents((prev) => prev.filter((s) => s.id !== id));
              setShowAlert(false);
          })
          .catch((err) => {
              console.error("Błąd:", err);
              setShowAlert(false);
          });
  };

  const confirmDelete = () => {
      if (selectedStudentId) {
          deleteUser(selectedStudentId);
      }
  };

  useEffect(() => {
    fetch("http://localhost:8080/api/auth/students")
      .then((res) => {
        if (!res.ok) throw new Error("Błąd sieci");
        return res.json();
      })
      .then((data) => setStudents(data))
      .catch((err) => {
        console.error("Błąd podczas pobierania studentów:", err);
      });
  }, []);

  return (
      <div>
          <Menu />
          <div className="container">
              <h2 className="title">Lista studentów</h2>
              {students.length === 0 ? (
                  <p>Brak studentów do wyświetlenia.</p>
              ) : (
                  <ul className="list">
                      {students.map((user) => (
                          <li key={user.id} className="item">
                              <span className="username">{user.username}</span>
                              {/*<span className="role">({user.role})</span>*/}
                              <img src={"images/delete.png"} alt="Usuń"
                                onClick={() => {
                                  setSelectedStudentId(user.id);
                                  setShowAlert(true);
                                }
                              }/>
                          </li>
                      ))}
                  </ul>
              )}
          </div>
          {showAlert && (
              <AlertModal
                  message="Czy na pewno chcesz usunąć użytkownika?"
                  onClose={() => setShowAlert(false)}
                  onConfirm={confirmDelete}
              />
          )}
      </div>

  );
}