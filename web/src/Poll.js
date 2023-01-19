import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { FormControl, FormControlLabel, Radio, Button } from '@material-ui/core';
import Alert from '@material-ui/lab/Alert';
import Typography from '@mui/material/Typography';

function Poll() {
    const [vote, setVote] = useState('');
    const [topic, setTopic] = useState('');
    const [error, setError] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    const navigator = useNavigate();

    async function voteInPool() {
        const response = await fetch(`http://52.23.164.7:8080/poll?vote=${vote}`,
            {
                method: 'PUT',
                headers: {
                    "Content-Type": "application/json"
                  },
                  credentials: 'include'
            });

        const message = await response.text();
        if (message === 'Vote computed!') {
            navigator('/results');
        } else {
            console.log(message);
            setError(true);
            setErrorMessage(message);
        }
    }

    useEffect(() => {
        async function getPool() {
            const response = await fetch('http://52.23.164.7:8080/poll/opened');

            const { topicName } = await response.json();
            setTopic(topicName)
            console.log(topicName);
        }
        getPool();
    }, []);

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log(`Voto enviado: ${vote}`);
        voteInPool();
    }

    return (
        <div className="container">
            {error && <Alert severity="error">{errorMessage}</Alert>}
            <Typography variant="h3" gutterBottom>
                {topic}
            </Typography>
            <FormControl component="fieldset">
                <FormControlLabel
                    value="yes"
                    control={<Radio color="primary" checked={vote === 'yes'} onChange={e => setVote(e.target.value)} />}
                    label="Sim"
                    labelPlacement="end"
                />
                <FormControlLabel
                    value="no"
                    control={<Radio color="primary" checked={vote === 'no'} onChange={e => setVote(e.target.value)} />}
                    label="Não"
                    labelPlacement="end"
                />
            </FormControl>
            <div className='button'>
                <Button variant="contained" color="primary" type="submit" onClick={handleSubmit}>
                    Votar
                </Button>
            </div>
        </div>
    );
}

export default Poll;