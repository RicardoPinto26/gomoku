import {useNavigate} from "react-router-dom";
import React from "react";
import {Card, CardActionArea, CardActions, CardContent, CardMedia, Fab} from "@mui/material";
import Typography from "@mui/material/Typography";
import {Lobby} from "../../../domain/Lobby";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import SportsEsportsIcon from '@mui/icons-material/SportsEsports';

interface LobbyCardProps {
    lobby: Lobby
    onJoinClick: () => void
}

export function LobbyCard(props: LobbyCardProps) {
    const lobby = props.lobby

    // Ugly AF, Luigi do your magic 🪄

    return (
        <Card variant="outlined">
            <CardContent>
                <Typography>Creator: {lobby.user1.username}</Typography>
                <Typography>Grid Size: {lobby.gridSize}</Typography>
                <Typography>Opening: {lobby.opening}</Typography>
                <Typography>Winning Length: {lobby.winningLength}</Typography>
                <Typography>Overflow: {lobby.overflow ? 'Yes' : 'No'}</Typography>
                <Fab color="primary" aria-label="add" onClick={props.onJoinClick}>
                    <SportsEsportsIcon />
                </Fab>
            </CardContent>
        </Card>
    )
}