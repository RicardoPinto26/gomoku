import {useNavigate, useParams} from "react-router-dom";
import {GameServices} from "../../../services/game/GameServices";
import React, {useEffect} from "react";
import {Game} from "../../../domain/game/Game";
import {GameBoard} from "./Gameplay";
import LoadingSpinner from "../../common/LoadingSpinner";
import {handleRequest} from "../../../services/utils/fetchSiren";
import {handleError} from "../../../services/utils/errorUtils";
import {getLobby} from "../../../services/lobby/LobbyServices";
import {Lobby} from "../../../domain/Lobby";
import {from} from "../matchmake/GameSettings";

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

        const [errorGame, res] = await handleRequest(GameServices.getGame(lId, gId))
        if (errorGame) {
            handleError(errorGame, setErrorGame, navigate)
            return
        }

        const [errorLobby, res2] = await handleRequest(getLobby(lId))
        if(errorLobby) {
            handleError(errorLobby, setErrorLobby, navigate)
            return
        }

        const lobby = new Lobby(res2.properties!)
        const gameSettings = from(lobby)
        const game = new Game(res.properties!, gameSettings)

        setGame(game)
        setGameLoaded(true)
    }

    if (gameLoaded)
        return <GameBoard game={game!} params={{lId: parseInt(lobbyId!)!, gId: parseInt(gameId!)}}
                          setGame={setGame}></GameBoard>
    else {
        return (
            <LoadingSpinner text={"Loading Game..."}/>
        )
    }
}