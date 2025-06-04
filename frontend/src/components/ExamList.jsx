export default function ExamList({ exams }) {
  return (
    <div className="exam-list">
      <h2>Egzaminy:</h2>
      <ul>
        {exams.map((exam) => (
          <li key={exam.id}>
            <span>{exam.name}</span>
            <span>{exam.date}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}