import React from "react";
import { useNavigate } from "react-router-dom";
import Menu from "../components/Menu";
import "../css/Settings.scss";

export default function Settings() {
    const navigate = useNavigate();

    const logout = () => {
        localStorage.removeItem("token");
        navigate("/login"); // przekierowanie po wylogowaniu
    };

    return (
        <div>
            <Menu />
            <div className="container">
                <h2 className="title">Ustawienia</h2>
                <div className="settings-section">
                    <button onClick={logout} className="logout-button">
                        Wyloguj się
                    </button>
                </div>
            </div>
        </div>
    );
}
