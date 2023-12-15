import {useParams} from "react-router-dom";
import {GameServices} from "../../../services/game/GameServices";
import React, {useEffect} from "react";
import {Game, GameDetailsOutputModel} from "../../../domain/game/Game";
import {GameBoard} from "./Game";
import LoadingSpinner from "../../common/LoadingSpinner";

export default function Gameplay() {
    const {lobbyId, gameId} = useParams()
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
        const res = await (GameServices.getGame(parseInt(lobbyId!), parseInt(gameId!)))
        const game = new Game(res.properties as GameDetailsOutputModel)

        setGame(game)
        setGameLoaded(true)
    }

    if (gameLoaded)
        return <GameBoard game={game!} params={{lId: parseInt(lobbyId!)!, gId: parseInt(gameId!)}} setGame={setGame}></GameBoard>
    else {
        return (
            <LoadingSpinner text={"Loading Game..."}/>
        )
    }
}