import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../css/Login.scss";
import Alert from "../components/Alert.jsx";

/**
 * Komponent logowania użytkownika.
 * @component
 * @returns {JSX.Element} Formularz logowania z obsługą autoryzacji.
 */
export default function Login() {
  /**
   * @type {[string, function]} Stan przechowujący nazwę użytkownika
   */
  const [username, setUsername] = useState("");
  /**
   * @type {[string, function]} Stan przechowujący hasło użytkownika
   */
  const [password, setPassword] = useState("");
  /**
   * @type {[boolean, function]} Stan przechowujący widoczność alertu błędu
   */
  const [showAlert, setShowAlert] = useState(false);
  /**
   * Hook do programowej nawigacji po ścieżkach w aplikacji
   * @type {function}
   */
  const navigate = useNavigate();

  /**
   * Funkcja obsługująca przesłanie formularza logowania.
   * Wysyła dane do API i na podstawie odpowiedzi loguje użytkownika lub pokazuje błąd.
   * @param {React.FormEvent<HTMLFormElement>} e - zdarzenie submit formularza
   * @async
   */
  const handleSubmit = async (e) => {
    e.preventDefault();

    const response = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    if (response.ok) {
      const data = await response.json();
      // Zapis tokena JWT do localStorage
      localStorage.setItem("token", data.token);
      // Zapis roli użytkownika (np. ADMIN, STUDENT)
      localStorage.setItem("role", data.role);
      // Przekierowanie na stronę dashboard po poprawnym logowaniu
      navigate("/dashboard");
    } else {
      // Pokazanie alertu błędu przy niepoprawnym logowaniu
      setShowAlert(true);
    }
  };

  return (
      <div>
        {/* Warunkowe renderowanie komponentu Alert z komunikatem o błędzie */}
        {showAlert && (
            <Alert
                message="Niepoprawny login lub hasło!"
                onClose={() => setShowAlert(false)}
            />
        )}

        {/* Pasek menu z logo i nazwą aplikacji */}
        <div className="menu">
          <div to="/login" style={{ marginRight: 0 }}>
            <img src="/images/logo.png" alt="logo" />
            <h1>examino</h1>
          </div>
        </div>

        {/* Formularz logowania */}
        <div className="auth-form">
          <h1>Logowanie</h1>
          <form onSubmit={handleSubmit}>
            {/* Pole na nazwę użytkownika */}
            <input
                type="text"
                placeholder="Nazwa użytkownika"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
            />
            {/* Pole na hasło */}
            <input
                type="password"
                placeholder="Hasło"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
            />
            {/* Przycisk do logowania */}
            <button type="submit">Zaloguj</button>
            <br />
            <br />
            <hr />
            <br />
            {/* Link do strony rejestracji z przyciskiem */}
            <Link to="/register">
              <button type="submit">Zarejestruj się</button>
            </Link>
          </form>
        </div>
      </div>
  );
}
