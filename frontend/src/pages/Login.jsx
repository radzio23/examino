import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../css/Login.scss";
import Alert from "../components/Alert.jsx";

export default function Login() {
  // Stan przechowujący nazwę użytkownika
  const [username, setUsername] = useState("");
  // Stan przechowujący hasło użytkownika
  const [password, setPassword] = useState("");
  // Stan przechowujący widoczność alertu
  const [showAlert, setShowAlert] = useState(false);
  // Hook do nawigacji po różnych stronach
  const navigate = useNavigate();

  // Funkcja obsługująca przesłanie formularza logowania
  const handleSubmit = async (e) => {
    e.preventDefault();

    const response = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem("token", data.token);
      localStorage.setItem("role", data.role);     //zapisujemy role
      navigate("/dashboard");
    } else {
      setShowAlert(true);
    }
  };


  return (
    <div>
      {showAlert && (<Alert message="Niepoprawny login lub hasło!" onClose={() => setShowAlert(false)}/>)}
      <div className="menu">
        <div to="/login" style={{marginRight: 0}}>
          <img src="/images/logo.png" alt="logo"/>
          <h1>examino</h1>
        </div>
      </div>

      <div className="auth-form">
        <h1>Logowanie</h1>
        {/* formularz logowania */}
        <form onSubmit={handleSubmit}>
          {/* nazwa użytkownika */}
          <input
              type="text"
              placeholder="Nazwa użytkownika"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
          />
          {/* hasło */}
          <input
              type="password"
              placeholder="Hasło"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
          />
          {/* przycisk do logowania */}
          <button type="submit">Zaloguj</button>
          <br/><br/>
          <hr/>
          <br/>
          <Link to="/register">
            <button type="submit">Zarejestruj się</button>
          </Link>
        </form>
      </div>
    </div>
  );
}
