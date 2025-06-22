import React from 'react';
import "../css/Alert.scss";

/**
 * Komponent modalnego alertu, który wyświetla komunikat użytkownikowi.
 * Może służyć jako potwierdzenie (z przyciskami "Zatwierdź" i "Anuluj")
 * lub prosty komunikat z przyciskiem "OK".
 *
 * @component
 * @param {Object} props - Właściwości komponentu
 * @param {string} props.message - Treść komunikatu do wyświetlenia
 * @param {function} props.onClose - Funkcja wywoływana po zamknięciu modala
 * @param {function} [props.onConfirm] - (Opcjonalnie) funkcja wywoływana po zatwierdzeniu
 * @returns {JSX.Element} Komponent modalnego alertu
 */
const AlertModal = ({ message, onClose, onConfirm }) => (
    <div className="alert">
        <div className="modal-content">
            <h2>Uwaga</h2>
            <p>{message}</p>
            <div className="buttons">
                {onConfirm ? (
                    <>
                        <button onClick={onConfirm}>Zatwierdź</button>
                        <button onClick={onClose}>Anuluj</button>
                    </>
                ) : (
                    <button onClick={onClose}>OK</button>
                )}
            </div>
        </div>
    </div>
);

export default AlertModal;
