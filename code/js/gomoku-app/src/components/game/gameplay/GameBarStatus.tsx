import React, {useEffect, useState} from 'react';
import {Box, LinearProgress, Typography} from '@mui/material';
import IconButton from "@mui/material/IconButton";
import RefreshIcon from "@mui/icons-material/Refresh";

interface GameBarProps {
    currentPlayer: string
    gameState: string
    player: string
    onRefresh: () => void
}

export function GameBarStatus(game: GameBarProps) {
    /*const [timer, setTimer] = useState(30)

    useEffect(() => {
        const interval = setInterval(() => {
            setTimer(prevTimer => prevTimer > 0 ? prevTimer - 1 : 0)
        }, 1000)

        return () => clearInterval(interval)
    }, [game.currentPlayer])*/

    return (
        <Box display="flex" justifyContent="space-between" alignItems="center" p={2} bgcolor="#f0f0f0">
            <Typography variant="h6">{game.gameState}</Typography>
            <IconButton onClick={game.onRefresh}>
                <RefreshIcon/>
            </IconButton>

            <Typography variant="h6"> {`${game.currentPlayer == game.player ? "Your turn" : `Turn: ${game.currentPlayer}`}`}</Typography>

        </Box>
    );
}

/**
 * <Box width="100%" mx={2}>
 *                 <LinearProgress variant="determinate" value={(timer / 30) * 100} />
 *             </Box>
            <Typography variant="subtitle1">{timer} seconds left</Typography>
 */
