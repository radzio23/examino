import { Link, useNavigate } from "react-router-dom";
import "../css/Menu.scss";
import { useEffect, useState } from "react";

/**
 * @component Menu
 * @description
 * Komponent menu nawigacyjnego, wyświetlający różne linki w zależności od roli użytkownika.
 * Pozwala na wylogowanie i przekierowanie do strony logowania.
 */
export default function Menu() {
    /** @state {string} role - aktualna rola użytkownika pobierana z localStorage */
    const [role, setRole] = useState(localStorage.getItem("role") || "STUDENT");

    /** @constant {function} navigate - funkcja do nawigacji między stronami */
    const navigate = useNavigate();

    /**
     * Efekt uruchamiany przy montowaniu komponentu,
     * ustawia aktualną rolę użytkownika na podstawie localStorage
     */
    useEffect(() => {
        setRole(localStorage.getItem("role") || "STUDENT");
        console.log("Aktualna rola w Dashboard:", localStorage.getItem("role"));
    }, []);

    /**
     * Funkcja wylogowująca użytkownika,
     * usuwa token z localStorage i przekierowuje do strony logowania
     */
    const logout = () => {
        localStorage.removeItem("token");
        navigate("/login"); // przekierowanie po wylogowaniu
    };

    return (
        <div className="menu">
            {/* Link do dashboard z logo */}
            <Link to="/dashboard">
                <img src="/images/logo.png" alt="logo" />
                <h1>examino</h1>
            </Link>

            {/* Link do egzaminów */}
            <Link to="/exams">Egzaminy</Link>

            {/* Warunkowy link do użytkowników (dla ADMIN) lub do wyników (dla STUDENT) */}
            {role === "ADMIN" ? (
                <Link to="/users">Użytkownicy</Link>
            ) : (
                <Link to="/results">Wyniki</Link>
            )}

            {/* Link wylogowania */}
            <a onClick={logout}>Wyloguj</a>
        </div>
    );
}
