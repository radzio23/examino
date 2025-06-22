import { useEffect, useState } from "react";
import Menu from "../components/Menu";
import '../css/Users.scss';
import AlertModal from "../components/Alert";

/**
 * Komponent Users wyświetla listę studentów i umożliwia ich usuwanie.
 * Pobiera listę studentów z backendu oraz obsługuje potwierdzenie usunięcia użytkownika.
 *
 * @component
 * @returns {JSX.Element} Widok listy studentów z opcją usuwania
 */
export default function Users() {
    // Stan przechowujący listę studentów
    const [students, setStudents] = useState([]);
    // Stan widoczności modala potwierdzenia usunięcia
    const [showAlert, setShowAlert] = useState(false);
    // Przechowuje ID wybranego studenta do usunięcia
    const [selectedStudentId, setSelectedStudentId] = useState(null);

    /**
     * Funkcja usuwająca użytkownika o podanym ID z serwera
     * @param {string} id - ID użytkownika do usunięcia
     */
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
                // Aktualizacja listy studentów po usunięciu
                setStudents((prev) => prev.filter((s) => s.id !== id));
                setShowAlert(false);
            })
            .catch((err) => {
                console.error("Błąd:", err);
                setShowAlert(false);
            });
    };

    /**
     * Funkcja wywoływana po potwierdzeniu usunięcia studenta
     */
    const confirmDelete = () => {
        if (selectedStudentId) {
            deleteUser(selectedStudentId);
        }
    };

    /**
     * Efekt pobierający listę studentów z backendu po załadowaniu komponentu
     */
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
                                <img
                                    src={"images/delete.png"}
                                    alt="Usuń"
                                    onClick={() => {
                                        setSelectedStudentId(user.id);
                                        setShowAlert(true);
                                    }}
                                />
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
