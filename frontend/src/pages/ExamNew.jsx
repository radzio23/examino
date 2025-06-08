import React from "react";
import { useNavigate } from "react-router-dom";
import ExamForm from "../components/ExamForm";

export default function ExamNew() {
  const navigate = useNavigate();

  const handleSaved = () => {
    navigate("/exams");
  };

  const handleClose = () => {
    navigate("/exams");
  };

  return (
    <div>
      <ExamForm onSaved={handleSaved} onClose={handleClose} />
    </div>
  );
}
