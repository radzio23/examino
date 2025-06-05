import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";

export default function Login() {
  // Stan przechowujący nazwę użytkownika
  const [username, setUsername] = useState("");
  // Stan przechowujący hasło użytkownika
  const [password, setPassword] = useState("");
  // Hook do nawigacji po różnych stronach
  const navigate = useNavigate();

  // Funkcja obsługująca przesłanie formularza logowania
  const handleSubmit = async (e) => {
    e.preventDefault(); // zapobiega domyślnemu przeładowaniu strony

    // Wysyłamy zapytanie POST do backendu z danymi logowania
    const response = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }), // wysyłamy nazwę i hasło jako JSON
    });

    if (response.ok) {
      // Jeśli odpowiedź jest OK, pobieramy dane (username i role) z odpowiedzi JSON
      const data = await response.json();
      // Przekierowujemy do /dashboard, przekazując rolę użytkownika w stanie nawigacji
      navigate("/dashboard", { state: { role: data.role } });
    } else {
      // Jeśli logowanie nie powiodło się, wyświetlamy alert z komunikatem
      alert("Niepoprawny login lub hasło");
    }
  };

  return (
    <div className="auth-form">
      <h2>Logowanie</h2>
      {/* Formularz logowania z obsługą submit */}
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
        {/* Przycisk do wysłania formularza */}
        <button type="submit">Zaloguj się</button>
      </form>
      <p>
        Nie masz konta? <Link to="/register">Utwórz je</Link> {/* Link do rejestracji */}
      </p>
    </div>
  );
}
