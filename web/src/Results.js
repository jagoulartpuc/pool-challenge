import React, { useState, useEffect } from 'react';
import { Table, TableRow, TableCell, TableBody } from '@material-ui/core';
import Typography from '@mui/material/Typography';
import './Results.css';

const Results = () => {
    const [opened, setOpened] = useState(true);
    const [countYes, setCountYes] = useState(0);
    const [countNo, setCountNo] = useState(0);

    useEffect(() => {
        async function getPool() {
            const response = await fetch('http://localhost:8080/poll/last');

            const poll = await response.json();
            if (!poll.opened) {
                setOpened(false);
                setCountYes(poll.countYes)
                setCountNo(poll.countNo)

            }
            console.log(poll);
        }
        getPool();
    }, []);

    return (
        <>
            {!opened &&
                <div className="container">
                    <Table>
                        <TableBody>
                            <TableRow>
                                <TableCell>Sim</TableCell>
                                <TableCell>{countYes}</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell>Não</TableCell>
                                <TableCell>{countNo}</TableCell>
                            </TableRow>
                        </TableBody>
                    </Table>
                </div>
            } 
            {opened &&
                <div className="container">
                    <Typography variant="h3" gutterBottom>
                        A votação não encerrou, por favor atualize a página para uma nova verificação.
                    </Typography>
                </div>
            }
        </>
    );
};

export default Results;