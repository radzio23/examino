import { useLocation } from "react-router-dom";
import Menu from "./Menu";          // menu dla ADMIN
import StudentMenu from "./StudentMenu";  // menu dla STUDENT 

export default function Dashboard() {
  const location = useLocation();
  const role = location.state?.role || "STUDENT"; // jeśli brak roli, to domyślnie STUDENT

  return (
    <div>
      {role === "ADMIN" ? <Menu /> : <StudentMenu />}
      <h1>Examino assasino</h1>
    </div>
  );
}
