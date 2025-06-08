import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import "./style.scss";
import Exam from "./pages/Exam";
import Dashboard from "./pages/Dashboard";
import Register from "./pages/Register";
import Login from "./pages/Login";
import Users from './pages/Users';
import ExamNew from './pages/ExamNew';
function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Domyślnie strona logowania */}
        <Route path="/" element={<Navigate to="/login" />} />

        {/* Logowanie i rejestracja */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Twoje istniejące strony */}
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/exams" element={<Exam />} />
        <Route path="/users" element={<Users />} />
        {/*strona do tworzenia egzaminu */}
        <Route path="/exams/new" element={<ExamNew />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
