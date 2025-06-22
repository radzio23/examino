import React, { useEffect, useState } from "react";
import Menu from "../components/Menu";
import "../css/Results.scss";

/**
 * Komponent wyświetlający wyniki użytkownika.
 * Pobiera wyniki z API i umożliwia ich filtrowanie po nazwie egzaminu lub przedmiocie.
 *
 * @component
 * @returns {JSX.Element} Widok z tabelą wyników użytkownika
 */
export default function MyResults() {
    // Stan przechowujący listę wyników użytkownika
    const [results, setResults] = useState([]);
    // Stan informujący o ładowaniu danych
    const [loading, setLoading] = useState(true);
    // Stan przechowujący ewentualny błąd podczas pobierania danych
    const [error, setError] = useState(null);
    // Stan przechowujący aktualną wartość pola wyszukiwania
    const [searchTerm, setSearchTerm] = useState("");

    // Efekt pobierający wyniki użytkownika przy załadowaniu komponentu
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
                setResults(data);    // Ustawienie pobranych wyników
                setLoading(false);   // Zakończenie ładowania
            })
            .catch((err) => {
                setError(err.message);  // Ustawienie komunikatu o błędzie
                setLoading(false);
            });
    }, []);

    /**
     * Formatuje znacznik czasu na czytelną datę i godzinę
     * @param {string|number} timestamp - znacznik czasu do sformatowania
     * @returns {string} sformatowana data w formacie polskim (dd.mm.rrrr, hh:mm)
     */
    const formatDate = (timestamp) => {
        const date = new Date(timestamp);
        return date.toLocaleString("pl-PL", {
            dateStyle: "short",
            timeStyle: "short",
        });
    };

    // Wyświetlanie informacji o ładowaniu
    if (loading) return <div>Ładowanie wyników...</div>;
    // Wyświetlanie komunikatu o błędzie, jeśli wystąpił
    if (error) return <div>Błąd: {error}</div>;

    return (
        <div>
            {/* Menu nawigacyjne */}
            <Menu />
            <div className="results">
                <div className="resultsMenu">
                    <h1>Twoje wyniki:</h1>
                    {/* Pole wyszukiwania do filtrowania wyników */}
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
                    {/* Tabela z wynikami */}
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
                            // Filtrowanie wyników po nazwie egzaminu lub przedmiocie (ignorując wielkość liter)
                            .filter((r) =>
                                r.examName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                                r.subject?.toLowerCase().includes(searchTerm.toLowerCase())
                            )
                            // Mapowanie wyników na wiersze tabeli
                            .map((r, i) => (
                                <tr key={i}>
                                    <td>{r.examName}</td>
                                    <td>{r.subject}</td>
                                    {/* Kolorowanie wyniku: zielony jeśli >=50%, czerwony jeśli mniej */}
                                    <td
                                        className={`scoreCell ${r.score >= 50 ? "pass" : "fail"}`}
                                    >
                                        {r.score.toFixed(2)}%
                                    </td>
                                    {/* Formatowana data ukończenia egzaminu */}
                                    <td>{formatDate(r.timestamp)}</td>
                                </tr>
                            ))
                        }
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}
