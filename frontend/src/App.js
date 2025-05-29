import React, { useEffect, useState } from 'react';

function App() {
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetch('http://localhost:8080/api/hello')  // To endpoint, który musisz stworzyć w backendzie
      .then(res => res.text())
      .then(text => setMessage(text));
  }, []);

  return (
    <div>
      <h1>Examino</h1>
      <p>Backend mówi: {message}</p>
    </div>
  );
}

export default App;