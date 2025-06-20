import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import "./css/Main.scss";
import Exam from "./pages/Exam";
import Register from "./pages/Register";
import Login from "./pages/Login";
import Users from './pages/Users';
import ExamDetails from './components/ExamDetails';
import ExamSolver from './components/ExamSolver';
import Results from './pages/Results';
function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Domyślnie strona logowania */}
        <Route path="/" element={<Navigate to="/login" />} />
s
        {/* Logowanie i rejestracja */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Istniejące strony */}
        <Route path="/dashboard" element={<Exam />} />
        <Route path="/exams" element={<Exam />} />
        <Route path="/users" element={<Users />} />
        <Route path="/exams/:examId/solve" element={<ExamSolver />} />
        <Route path="/results" element={<Results />} />

        <Route path="/exams/:id" element={<ExamDetails />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
