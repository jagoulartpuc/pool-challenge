import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { TextField, Button } from '@material-ui/core';
import Alert from '@material-ui/lab/Alert';
import './Login.css';


function Login() {
    const [cpf, setCpf] = useState('');
    const [error, setError] = useState(false);
    const navigator = useNavigate();

    async function login() {
        const response = await fetch(`http://localhost:8080/login?cpf=${cpf}`,
            {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json"
                  },
                  credentials: 'include'
            });

        const json = await response.text();
        if (json == 'Logged in!') {
            navigator('/poll');
        } else {
            setError(true);
        }
        console.log(json);
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        login();
    }

    return (
        <div className="container">
            {error && <Alert severity="error">CPF não encontrado</Alert>}
            <TextField
                label="CPF"
                value={cpf}
                onChange={e => setCpf(e.target.value)}
            />
            <div className="button">
                <Button variant="contained" color="primary" type="submit" onClick={handleSubmit}>
                    Login
                </Button>
            </div>
        </div>
    );
}

export default Login;
