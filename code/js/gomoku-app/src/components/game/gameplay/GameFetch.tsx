import {useNavigate, useParams} from "react-router-dom";
import {GameServices} from "../../../services/game/GameServices";
import React, {useEffect} from "react";
import {Game} from "../../../domain/game/Game";
import {GameBoard} from "./Gameplay";
import LoadingSpinner from "../../common/LoadingSpinner";
import {handleRequest} from "../../../services/utils/fetchSiren";
import {handleError} from "../../../services/utils/errorUtils";
import {Lobby} from "../../../domain/Lobby";
import {from} from "../matchmake/GameSettings";
import {LobbyServices} from "../../../services/lobby/LobbyServices";

export default function GameFetch() {
    const {lobbyId, gameId} = useParams()
    const navigate = useNavigate()
    let [errorGame, setErrorGame] = React.useState<string | null>(null)
    let [errorLobby, setErrorLobby] = React.useState<string | null>(null)
    const [gameLoaded, setGameLoaded] = React.useState(false);
    const [game, setGame] = React.useState<Game | null>(null);

    useEffect(() => {
        if (!gameLoaded) {
            fetchGame()
        }
    }, [gameLoaded])


    //martelo
    async function fetchGame() {
        if (lobbyId === undefined || gameId === undefined) {
            throw new Error("Lobby ID or Game ID is undefined")
        }
        const gId = parseInt(gameId!)
        const lId = parseInt(lobbyId!)

        const [errGame, res] = await handleRequest(GameServices.getGame(lId, gId))
        if (errGame) {
            handleError(errGame, setErrorGame, navigate)
            return
        }

        const [errLobby, res2] = await handleRequest(LobbyServices.getLobby(lId))
        if (errLobby) {
            handleError(errLobby, setErrorLobby, navigate)
            return
        }

        const lobby = new Lobby(res2.properties!)
        const gameSettings = from(lobby)
        const game = new Game(res.properties!, gameSettings)

        setGame(game)
        setGameLoaded(true)
    }

    if (errorGame || errorLobby) {
        return <div>Error: {errorGame || errorLobby}</div>
    } else if (gameLoaded)
        return <GameBoard game={game!} params={{lId: parseInt(lobbyId!)!, gId: parseInt(gameId!)}}
                          setGame={setGame}></GameBoard>
    else {
        return (
            <LoadingSpinner text={"Loading Game..."}/>
        )
    }
}