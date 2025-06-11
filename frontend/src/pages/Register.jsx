import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../css/Login.scss";
import Alert from "../components/Alert";

export default function Register() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showAlert, setShowAlert] = useState(false);
  const [alertMessage, setMessage] = useState("");
  const [canGo, setGo] = useState(false);


  useEffect(() => {
    if (!showAlert && canGo){
      navigate("/login");
    }
  }, [showAlert, canGo]);

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Walidacja danych
    if (!username.trim() || !password.trim()) {
      setMessage("Podaj login i hasło!")
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

      // Dodatkowe logowanie dla debugowania
      console.log("Request payload:", { username: username.trim(), password: password.trim() });
      
      if (response.ok) {
        setMessage("Zarejestrowano pomyślnie! Możesz się zalogować.")
        setShowAlert(true);
        setGo(true);
      } else {
        const errorData = await response.json();
        setMessage("Użytkownik o podanej nazwie już istnieje!");
        setShowAlert(true);
        console.log("Backend error:", errorData);
      }
    } catch (err) {
      setMessage("Błąd sieci. Spróbuj ponownie później.");
      setShowAlert(true);
      console.log("Network error:", err);
    }
  };

  return (
    <div>
      {showAlert && (<Alert message={alertMessage} onClose={() => setShowAlert(false)}/>)}
      <div className="menu">
        <div to="/login" style={{marginRight: 0}}>
          <img src="/images/logo.png" alt="logo"/>
          <h1>examino</h1>
        </div>
      </div>

      <div className="auth-form">
        <h1>Rejestracja</h1>
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
          <button type="submit">Zarejestruj</button>
        </form>
        <br/>
        <p>Masz już konto? <Link to="/login">Zaloguj się</Link></p>
      </div>
    </div>
  );
}