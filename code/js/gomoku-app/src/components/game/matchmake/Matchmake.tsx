import React, {useEffect} from "react";

import Page from "../../common/Page";
import LoadingSpinner from "../../common/LoadingSpinner";
import {GameSettings, useMatchmakingConfig} from "./GameSettings";
import {useNavigate} from "react-router-dom";
import {useUserManager} from "../../../utils/Authn";
import {handleRequest} from "../../../services/utils/fetchSiren";
import {LobbyServices} from "../../../services/lobby/LobbyServices";
import {handleError} from "../../../services/utils/errorUtils";


export function Matchmake() {
    const navigate = useNavigate();
    const [error, setError] = React.useState<string | null>(null);
    const {settings} = useMatchmakingConfig();
    const [lobbyId, setLobbyId] = React.useState(-1);
    const [waitingForOpponent, setWaitingForOpponent] = React.useState(false);

    const userManager = useUserManager();

    useEffect(() => {
        console.log("MatchMake")
        matchMake(settings)

    }, [settings]);

    useEffect(() => {
        const interval = setInterval(() => {
            checkIfOpponentJoined().then(r => console.log(r));
        }, 2000);
        return () => clearInterval(interval);
    }, [lobbyId, waitingForOpponent]);


    async function matchMake(settings: GameSettings) {
        const [error, res] = await handleRequest(LobbyServices.matchMake(settings))

        if (error) {
            handleError(error, setError, navigate)
            return
        }
        if (res == undefined) {
            throw new Error("Response is undefined")
        }

        if (res.properties?.user2 != null && res.properties?.user2.username != userManager.user?.toString()) {
            console.log("Matchmake success - joined lobby");
            // @ts-ignore - TODO() - siren link
            navigate(res.entities[0].links[0].href.replace("/api", ""))
        } else {
            console.log("Matchmake success - created lobby");
            setLobbyId(res.properties?.lobbyId as number)
        }

        setWaitingForOpponent(true);
    }

    async function checkIfOpponentJoined() {
        if (!waitingForOpponent || lobbyId == -1) return;
        const [error, res] = await handleRequest(LobbyServices.getLobby(lobbyId))

        if (error) {
            handleError(error, setError, navigate)
            return
        }
        if (res == undefined) {
            throw new Error("Response is undefined")
        }

        if (res.properties?.user2 != null) {
            setWaitingForOpponent(false);
            // TODO() - siren link
            // @ts-ignore
            const gameId = res.entities[0].links[0].href.replace("/api", "");
            navigate(`${gameId}`);
        }

    }

    return (
        <Page title={"Gameplay Menu"}>
            <LoadingSpinner text={"Matchmaking..."}/>
            {error && <div style={{color: 'red'}}>{error}</div>}
        </Page>
    );
}