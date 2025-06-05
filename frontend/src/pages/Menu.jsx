import {Link} from "react-router-dom";

export default function Menu() {
  return (
    <div className="menu">
      <Link to="/">
        <img src="/images/exam.png" alt="logo"/>
        <h1>Examino</h1>
      </Link>
      <Link to="/exams">
        <h1>Egzaminy</h1>
      </Link>
      <Link to="/users">
        <h1>Użytkownicy</h1>
      </Link>
      <Link to="/settings">
        <h1>Ustawienia</h1>
      </Link>
    </div>
  );
}