import React from 'react';
import "../css/Alert.scss"

const AlertModal = ({ message, onClose }) => {
    return (
      <div className="alert">
        <div>
            <h2>Uwaga</h2>
            <p>{message}</p>
            <button onClick={onClose}>Zamknij</button>
        </div>
      </div>
    );
};

export default AlertModal;
