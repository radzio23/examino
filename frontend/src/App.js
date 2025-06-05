import {BrowserRouter, Routes, Route} from "react-router-dom";
import "./style.scss"
import Exam from "./pages/Exam";
import Dashboard from "./pages/Dashboard";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Dashboard/>}/>
        <Route path="/exams" element={<Exam/>} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;