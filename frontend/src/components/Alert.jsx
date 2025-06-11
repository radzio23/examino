import React from 'react';
import "../css/Alert.scss";

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
