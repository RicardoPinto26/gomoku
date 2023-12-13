import {useParams} from "react-router-dom";
import {GameServices} from "../../../services/game/GameServices";
import React, {useEffect} from "react";
import {Game} from "../../../domain/game/Game";
import {Board} from "../../../domain/game/Board";
import {User} from "../../../domain/User";
import {GameBoard} from "./Game";

export default function Gameplay() {
    const {lobbyId, gameId} = useParams()
    const [gameLoaded, setGameLoaded] = React.useState(false);
    const [game, setGame] = React.useState<Game | null>(null);

    useEffect(() => {
        if (!gameLoaded) {
            fetchGame().then(r => console.log(r))
        }
    }, [gameLoaded])


    //martelo
    async function fetchGame() {
        const res = await (GameServices.getGame(parseInt(lobbyId!), parseInt(gameId!)))
        const board = new Board(15, Board.convertJsonToBoard(res.properties.board))
        const turn = res.properties.turn
        const status = res.properties.status
        const user1 = new User(res.properties.user1.username, res.properties.user1.gamesPlayed, res.properties.user1.rating)
        const user2 = new User(res.properties.user2.username, res.properties.user2.gamesPlayed, res.properties.user2.rating)
        const game = new Game(parseInt(gameId!), board, status, user1, user2, turn, null)
        setGame(game)
        setGameLoaded(true)
    }

    if (gameLoaded)
        return <GameBoard game={game!} params={{lId: parseInt(lobbyId!)!, gId: parseInt(gameId!)}}></GameBoard>
    else {
        return (
            <div>
                <h1>Gomoku Gameplay</h1>
                <p>Lobby ID: {lobbyId}</p>
                <p>Game ID: {gameId}</p>
            </div>
        )
    }
}