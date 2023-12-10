import React, {useEffect} from "react";

import Page from "../../common/Page";
import LoadingSpinner from "../../common/LoadingSpinner";
import {GameSettings, useMatchmakingConfig} from "./GameSettings";
import Typography from "@mui/material/Typography";
import {useNavigate} from "react-router-dom";
import {apiUrl} from "../../../utils/configs";
import {getLobbyState} from "../../../services/lobby/LobbyServices";


export function Matchmake() {
    const {settings} = useMatchmakingConfig();
    const [inLobbyAlready, setInLobbyAlready] = React.useState(false);
    const [inGameAlready, setInGameAlready] = React.useState(false);
    const [lobbyId, setLobbyId] = React.useState(-1);
    const [waitingForOpponent, setWaitingForOpponent] = React.useState(false);

    const navigate = useNavigate();

    // HAMMER
    useEffect(() => {
        console.log("MatchMake");
        matchMake(settings).then(r => {
            console.log(r);
            if (r.status == 409) {
                console.log(r.response.type)
                switch (r.response.type) {
                    case "userAlreadyInALobby":
                        console.log("Already in lobby");
                        setInLobbyAlready(true);
                        break;
                    case "userAlreadyInAGame":
                        console.log("Already in game");
                        setInGameAlready(true);
                        break;
                }
            }
            if (r.status == 201) {
                console.log(`Matchmake success - ${r.response.properties.lobbyId} `);
                setLobbyId(r.response.properties.lobbyId);
            }
            if (r.status == 200) {
                console.log("Matchmake success - joined lobby");
                navigate(`/game/${r.response.properties.gameId}`);
            }
            setWaitingForOpponent(true);

        });
    }, [settings]);

    useEffect(() => {
        const interval = setInterval(() => {
            checkIfOpponentJoined().then(r => console.log(r));
        }, 2000);
        return () => clearInterval(interval);
    }, [lobbyId, waitingForOpponent]);

    async function matchMake(settings: GameSettings): Promise<{ status: number, response: any }> {
        const res = await fetch(`${apiUrl}/lobby/seek`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
            body: JSON.stringify(settings)
        });
        const body = await res.json();
        return {response: body, status: res.status};
    }

    async function checkIfOpponentJoined() {
        if (!waitingForOpponent || lobbyId == -1) return;

        try {
            const lobby = await getLobbyState(lobbyId);
            if (lobby.properties.user2 != null) {
                setWaitingForOpponent(false);
                const gameId = lobby.properties.gameId;
                console.log("Opponent joined, navigating to game");
                console.log(gameId);
                navigate(`/game/${gameId}}`);
            }
        } catch (error) {
            console.log("Error in checkIfOpponentJoined:", error);
        }
    }


    if (inLobbyAlready) {
        return (
            <Page title={"Gameplay Menu"}>
                <LoadingSpinner text={"Matchmaking..."}/>
                <Typography variant="h6" gutterBottom>
                    {"Already in lobby... Waiting for opponent"}
                </Typography>
            </Page>
        );
    }
    if (inGameAlready) {
        return (
            <Page title={"Gameplay Menu"}>
                <LoadingSpinner text={"Matchmaking..."}/>
                <Typography variant="h6" gutterBottom>
                    {"Already in game... Leave or finish your game first!"}
                </Typography>
            </Page>
        );
    } else {
        return (
            <Page title={"Gameplay Menu"}>
                <LoadingSpinner text={"Matchmaking..."}/>
            </Page>
        );
    }
}