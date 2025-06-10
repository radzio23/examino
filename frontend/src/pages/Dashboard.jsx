import { useState, useEffect } from "react";
import Menu from "./Menu";
import StudentMenu from "./StudentMenu";

export default function Dashboard() {
  const [role, setRole] = useState(localStorage.getItem("role") || "STUDENT");

  useEffect(() => {
    setRole(localStorage.getItem("role") || "STUDENT");
    console.log("Aktualna rola w Dashboard:", localStorage.getItem("role"));
  }, []);

  return (
    <div>
      {role === "ADMIN" ? <Menu /> : <StudentMenu />}
      <h1>Examino assasino</h1>
    </div>
  );
}
