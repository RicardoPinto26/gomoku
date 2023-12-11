import Button from "@mui/material/Button";
import {getLobbies, joinLobby} from "../../../services/lobby/LobbyServices";
import React, {useEffect} from "react";
import Page from "../../common/Page";
import LoadingSpinner from "../../common/LoadingSpinner";
import {Lobby} from "../../../domain/Lobby";
import {LobbyCard} from "./LobbyCard";
import IconButton from "@mui/material/IconButton";
import RefreshIcon from '@mui/icons-material/Refresh';
import {useNavigate} from "react-router-dom";
import Typography from "@mui/material/Typography";

export function JoinLobby() {
    const nav = useNavigate();
    const [lobbies, setLobbies] = React.useState([]);
    const [lobbiesLoaded, setLobbiesLoaded] = React.useState(false);

    useEffect(() => {
        if (!lobbiesLoaded) {
            fetchLobbies().then(r => console.log(r))
        }
    }, [lobbiesLoaded, lobbies])

    async function fetchLobbies() {
        const response = await getLobbies();
        setLobbies(response)
        setLobbiesLoaded(true);
    }

    async function handleJoinClick(lobbyId: number) {
        console.log(`Joining lobby ${lobbyId}`)
        const response = await joinLobby(lobbyId);
        if (response.joinned) {
            console.log("Joined lobby");
            console.log(response.response.entities[0]);
            const gameLink = response.response.entities[0].href
            console.log(gameLink);
            nav(gameLink)
        } else {
            console.log("Failed to join lobby");
            console.log(response.response);
        }
    }

    return (
        <Page title={"Available Lobbies"}>
            <IconButton onClick={fetchLobbies}>Refresh
                <RefreshIcon/>
            </IconButton>
            {lobbiesLoaded
                ? lobbies.length == 0
                    ? <Typography>No lobbies available</Typography>
                    :
                    lobbies.map((lobby: Lobby) => {
                        return <LobbyCard key={lobby.id} lobby={lobby} onJoinClick={() => {
                            handleJoinClick(lobby.id)
                        }}/>
                    }) : <LoadingSpinner text={"Loading lobbies..."}/>}
        </Page>
    )
}