import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../css/Login.scss";
import Alert from "../components/Alert";

/**
 * Komponent rejestracji nowego użytkownika.
 * @component
 * @returns {JSX.Element} Formularz rejestracji z walidacją i obsługą API.
 */
export default function Register() {
  /**
   * @type {[string, function]} Stan przechowujący nazwę użytkownika
   */
  const [username, setUsername] = useState("");
  /**
   * @type {[string, function]} Stan przechowujący hasło użytkownika
   */
  const [password, setPassword] = useState("");
  /**
   * @type {[boolean, function]} Stan widoczności alertu
   */
  const [showAlert, setShowAlert] = useState(false);
  /**
   * @type {[string, function]} Stan komunikatu wyświetlanego w alercie
   */
  const [alertMessage, setMessage] = useState("");
  /**
   * @type {[boolean, function]} Flaga decydująca, czy można przejść do strony logowania
   */
  const [canGo, setGo] = useState(false);

  /**
   * Hook uruchamiany przy zmianie wartości `showAlert` lub `canGo`.
   * Jeśli alert nie jest widoczny, a `canGo` jest true, następuje przekierowanie do logowania.
   */
  useEffect(() => {
    if (!showAlert && canGo) {
      navigate("/login");
    }
  }, [showAlert, canGo]);

  /**
   * Hook do programowej nawigacji po ścieżkach w aplikacji
   * @type {function}
   */
  const navigate = useNavigate();

  /**
   * Funkcja obsługująca przesłanie formularza rejestracji.
   * Wysyła dane do API, obsługuje błędy i wyświetla odpowiednie komunikaty.
   * @param {React.FormEvent<HTMLFormElement>} e - zdarzenie submit formularza
   * @async
   */
  const handleSubmit = async (e) => {
    e.preventDefault();

    // Walidacja - sprawdzenie czy pola nie są puste
    if (!username.trim() || !password.trim()) {
      setMessage("Podaj login i hasło!");
      setShowAlert(true);
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          username: username.trim(),
          password: password.trim()
        }),
      });

      // Logowanie danych wysyłanych do API (debug)
      console.log("Request payload:", { username: username.trim(), password: password.trim() });

      if (response.ok) {
        // Rejestracja powiodła się
        setMessage("Zarejestrowano pomyślnie! Możesz się zalogować.");
        setShowAlert(true);
        setGo(true);
      } else {
        // Obsługa błędu z backendu (np. użytkownik już istnieje)
        const errorData = await response.json();
        setMessage("Użytkownik o podanej nazwie już istnieje!");
        setShowAlert(true);
        console.log("Backend error:", errorData);
      }
    } catch (err) {
      // Obsługa błędu sieciowego
      setMessage("Błąd sieci. Spróbuj ponownie później.");
      setShowAlert(true);
      console.log("Network error:", err);
    }
  };

  return (
      <div>
        {/* Komponent alertu wyświetlany gdy showAlert jest true */}
        {showAlert && (<Alert message={alertMessage} onClose={() => setShowAlert(false)} />)}

        {/* Pasek menu z logo i nazwą aplikacji */}
        <div className="menu">
          <div to="/login" style={{ marginRight: 0 }}>
            <img src="/images/logo.png" alt="logo" />
            <h1>examino</h1>
          </div>
        </div>

        {/* Formularz rejestracji */}
        <div className="auth-form">
          <h1>Rejestracja</h1>
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
            {/* Przycisk do rejestracji */}
            <button type="submit">Zarejestruj</button>
          </form>
          <br />
          <p>Masz już konto? <Link to="/login">Zaloguj się</Link></p>
        </div>
      </div>
  );
}
