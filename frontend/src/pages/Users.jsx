import { useEffect, useState } from "react";
import Menu from "./Menu";

export default function Users() {
  const [students, setStudents] = useState([]);

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
    <div style={pageStyle}>
      <Menu />
      <div style={containerStyle}>
        <h2 style={titleStyle}>Lista studentów</h2>
        {students.length === 0 ? (
          <p>Brak studentów do wyświetlenia.</p>
        ) : (
          <ul style={listStyle}>
            {students.map((user) => (
              <li key={user.id} style={itemStyle}>
                <span style={usernameStyle}>{user.username}</span> 
                <span style={roleStyle}>({user.role})</span>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}

const pageStyle = {
  fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
  backgroundColor: "#f9fafb",
  minHeight: "100vh",
  padding: "20px",
};

const containerStyle = {
  maxWidth: "600px",
  margin: "40px auto",
  backgroundColor: "white",
  borderRadius: "8px",
  boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
  padding: "20px 30px",
};

const titleStyle = {
  color: "#333",
  marginBottom: "20px",
  borderBottom: "2px solid #007bff",
  paddingBottom: "8px",
};

const listStyle = {
  listStyleType: "none",
  padding: 0,
  margin: 0,
};

const itemStyle = {
  padding: "12px 15px",
  marginBottom: "10px",
  borderRadius: "6px",
  backgroundColor: "#e9f0ff",
  display: "flex",
  justifyContent: "space-between",
  alignItems: "center",
  transition: "background-color 0.3s ease",
  cursor: "default",
};

const usernameStyle = {
  fontWeight: "600",
  color: "#222",
};

const roleStyle = {
  fontStyle: "italic",
  color: "#555",
};