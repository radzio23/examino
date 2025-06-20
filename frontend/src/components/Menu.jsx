import {Link, useNavigate} from "react-router-dom";
import "../css/Menu.scss"
import {useEffect, useState} from "react";

export default function Menu() {
    const [role, setRole] = useState(localStorage.getItem("role") || "STUDENT");
    const navigate = useNavigate();

    useEffect(() => {
        setRole(localStorage.getItem("role") || "STUDENT");
        console.log("Aktualna rola w Dashboard:", localStorage.getItem("role"));
    }, []);

    const logout = () => {
        localStorage.removeItem("token");
        navigate("/login"); // przekierowanie po wylogowaniu
    };

  return (
    <div className="menu">
      <Link to="/dashboard">
        <img src="/images/logo.png" alt="logo"/>
          <h1>examino</h1>
      </Link>
      <Link to="/exams">Egzaminy</Link>
      {role === "ADMIN" ?
          <Link to="/users">Użytkownicy</Link> :
          <Link to="/results">Wyniki</Link>}
      <a onClick={logout}>Wyloguj</a>
    </div>
  );
}