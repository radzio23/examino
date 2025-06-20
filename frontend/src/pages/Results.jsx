import React, { useEffect, useState } from "react";
import Menu from "../components/Menu";
import "../css/Results.scss";

export default function MyResults() {
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [showForm, setShowForm] = useState(false);
    const [searchTerm, setSearchTerm] = useState("");

    useEffect(() => {
        const token = localStorage.getItem("token");

        fetch("http://localhost:8080/api/results/my", {
            headers: { Authorization: `Bearer ${token}` },
        })
            .then((res) => {
                if (!res.ok) throw new Error("Błąd podczas pobierania wyników");
                return res.json();
            })
            .then((data) => {
                setResults(data);
                setLoading(false);
            })
            .catch((err) => {
                setError(err.message);
                setLoading(false);
            });
    }, []);

    const formatDate = (timestamp) => {
        const date = new Date(timestamp);
        return date.toLocaleString("pl-PL", {
            dateStyle: "short",
            timeStyle: "short",
        });
    };

    if (loading) return <div>Ładowanie wyników...</div>;
    if (error) return <div>Błąd: {error}</div>;

    return (
        <div>
            <Menu />
            <div className="results">
                <div className="resultsMenu">
                    <h1>Twoje wyniki:</h1>
                    <input
                        type="text"
                        placeholder="Szukaj po nazwie lub przedmiocie..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="examSearchInput"
                    />
                </div>
                <hr/>
                <div className="resultsTableWrapper">
                    <table>
                        <thead>
                        <tr>
                            <th>Nazwa egzaminu</th>
                            <th>Przedmiot</th>
                            <th>Wynik</th>
                            <th>Zakończono</th>
                        </tr>
                        </thead>
                        <tbody>
                        {results
                            .filter((r) =>
                                r.examName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                                r.subject?.toLowerCase().includes(searchTerm.toLowerCase())
                            )
                                .map((r, i) => (
                            <tr key={i}>
                                <td>{r.examName}</td>
                                <td>{r.subject}</td>
                                <td
                                    className={`scoreCell ${r.score >= 50 ? "pass" : "fail"}`}
                                >
                                    {r.score.toFixed(2)}%
                                </td>
                                <td>{formatDate(r.timestamp)}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}
