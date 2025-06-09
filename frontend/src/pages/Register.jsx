import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../css/Login.css";

export default function Register() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    // Walidacja danych
    if (!username.trim() || !password.trim()) {
      setError("Nazwa użytkownika i hasło nie mogą być puste");
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

      // Dodatkowe logowanie dla debugowania
      console.log("Request payload:", { username: username.trim(), password: password.trim() });
      
      if (response.ok) {
        alert("Zarejestrowano pomyślnie. Możesz się zalogować.");
        navigate("/login");
      } else {
        const errorData = await response.json();
        setError(errorData.error || "Nieznany błąd serwera");
        console.error("Backend error:", errorData);
      }
    } catch (err) {
      setError("Problem z połączeniem sieciowym");
      console.error("Network error:", err);
    }
  };

  return (
    <div className="auth-form">
      <h2>Rejestracja</h2>
      {error && <div className="error-message">{error}</div>}
      
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Nazwa użytkownika"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Hasło"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Zarejestruj się</button>
      </form>
      <p>
        Masz już konto? <Link to="/login">Zaloguj się</Link>
      </p>
    </div>
  );
}