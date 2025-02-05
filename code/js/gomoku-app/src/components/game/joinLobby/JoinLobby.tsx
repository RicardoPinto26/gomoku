import React, {useEffect} from "react";
import Page from "../../common/Page";
import LoadingSpinner from "../../common/LoadingSpinner";
import {Lobby} from "../../../domain/Lobby";
import {LobbyCard} from "./LobbyCard";
import IconButton from "@mui/material/IconButton";
import RefreshIcon from '@mui/icons-material/Refresh';
import {useNavigate} from "react-router-dom";
import Typography from "@mui/material/Typography";
import {handleRequest} from "../../../services/utils/fetchSiren";
import {handleError} from "../../../services/utils/errorUtils";
import {EmbeddedSubEntity} from "../../../http/media/siren/SubEntity";
import {LobbyServices} from "../../../services/lobby/LobbyServices";

export function JoinLobby() {
    const nav = useNavigate();
    const [error, setError] = React.useState<string | null>(null);
    const [lobbies, setLobbies] = React.useState<EmbeddedSubEntity<Lobby>[]>([]);
    const [lobbiesLoaded, setLobbiesLoaded] = React.useState(false);

    useEffect(() => {
        if (!lobbiesLoaded) {
            fetchLobbies().then(r => console.log(r))
        }
    }, [lobbiesLoaded, lobbies])

    async function fetchLobbies() {
        const [error, res] = await handleRequest(LobbyServices.getLobbies())

        if (error) {
            handleError(error, setError, nav)
            return
        }
        if (res.entities === undefined) {
            throw new Error("Entities are undefined")
        }
        const lobbies = res.entities.map(entity => entity as EmbeddedSubEntity<Lobby>)
        setLobbies(lobbies)
        setLobbiesLoaded(true);
    }

    async function handleJoinClick(lobby: EmbeddedSubEntity<Lobby>) {
        const href = lobby.actions?.find(action => action.name == "join-lobby")?.href
        if(href === undefined) {
            throw new Error("Join lobby href is undefined")
        }
        const [err, res] = await handleRequest(LobbyServices.joinLobby(href))
        if(err) {
            handleError(err, setError, nav)
            return
        }
        if(res == undefined) {
            throw new Error("Response is undefined")
        }
        // @ts-ignore TODO() - siren link
        const gameLink = res.entities[0].href.replace("/api", "")
        nav(gameLink)
    }

    return (
        <Page title={"Available Lobbies"}>
            <IconButton onClick={fetchLobbies}>Refresh
                <RefreshIcon/>
            </IconButton>
            {error && <div style={{ color: 'red' }}>{error}</div>}
            {lobbiesLoaded
                ? lobbies.length == 0
                    ? <Typography>No lobbies available</Typography>
                    :
                    lobbies.map((lobby: EmbeddedSubEntity<Lobby>) => {
                        // @ts-ignore
                        return <LobbyCard key={lobby.properties.id} lobby={lobby.properties} onJoinClick={() => {
                            handleJoinClick(lobby)
                        }}/>
                    }) : <LoadingSpinner text={"Loading lobbies..."}/>}
        </Page>
    )
}