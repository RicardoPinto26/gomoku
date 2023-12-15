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
        // Board hardcoded for now
        const board = new Board(15, Board.convertJsonToBoard(res.properties.board))
        const turn = res.properties.turn
        const status = res.properties.status
        let { username: username1, gamesPlayed: gamesPlayed1, rating: rating1 } = res.properties.user1;
        let { username: username2, gamesPlayed: gamesPlayed2, rating: rating2 } = res.properties.user2;

        const user1 = new User(username1, gamesPlayed1, rating1);
        const user2 = new User(username2, gamesPlayed2, rating2);

        const game = new Game(parseInt(gameId!), board, status, user1, user2, turn, null)
        console.log(game)
        setGame(game)
        setGameLoaded(true)
    }

    if (gameLoaded)
        return <GameBoard game={game!} params={{lId: parseInt(lobbyId!)!, gId: parseInt(gameId!)}}></GameBoard>
    else {
        return (// test
            <div>
                <h1>Gomoku Gameplay</h1>
                <p>Lobby ID: {lobbyId}</p>
                <p>Game ID: {gameId}</p>
            </div>
        )
    }
}