import React from 'react';
import {Box, Typography} from '@mui/material';
import IconButton from "@mui/material/IconButton";
import RefreshIcon from "@mui/icons-material/Refresh";

interface GameBarProps {
    currentPlayer: string
    gameState: string
    player: string
    onRefresh: () => void
}

export function GameBarStatus(game: GameBarProps) {

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
