import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import "./css/Main.scss";
import Exam from "./pages/Exam";
import Register from "./pages/Register";
import Login from "./pages/Login";
import Users from './pages/Users';
import ExamDetails from './components/ExamDetails';
import ExamSolver from './components/ExamSolver';
import Results from './pages/Results';

/**
 * Główny komponent aplikacji definiujący routing pomiędzy stronami.
 * Korzysta z React Router do zarządzania nawigacją między widokami.
 *
 * @component
 * @returns {JSX.Element} Struktura routingu aplikacji
 */
function App() {
    return (
        <BrowserRouter>
            <Routes>
                {/* Przekierowanie domyślne na stronę logowania */}
                <Route path="/" element={<Navigate to="/login" />} />

                {/* Strony logowania i rejestracji */}
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />

                {/* Główne strony aplikacji */}
                <Route path="/dashboard" element={<Exam />} />
                <Route path="/exams" element={<Exam />} />
                <Route path="/users" element={<Users />} />
                <Route path="/exams/:examId/solve" element={<ExamSolver />} />
                <Route path="/results" element={<Results />} />

                {/* Szczegóły egzaminu */}
                <Route path="/exams/:id" element={<ExamDetails />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;
