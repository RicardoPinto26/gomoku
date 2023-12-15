import React, {useEffect, useState} from 'react';
import {BoardView} from './BoardView';
import {Game, GameDetailsOutputModel} from "../../../domain/game/Game";
import {GameServices} from "../../../services/game/GameServices";
import {ActionType, GamePlayInputModel} from "../../../services/game/modals/GamePlayInputModel";
import {useCurrentUser} from "../../../utils/Authn";
import Typography from "@mui/material/Typography";
import {initializeBoard} from "../../../domain/game/Board";
import RefreshIcon from "@mui/icons-material/Refresh";
import IconButton from "@mui/material/IconButton";

interface GameProps {
    game: Game
    params: { lId: number, gId: number }
    setGame: (game: Game) => void
}

export function GameBoard(game: GameProps) {
    const user = useCurrentUser()
    const boardSize = game.game.board.grid.length;
    const initialBoard = initializeBoard(boardSize)
    const [board, setBoard] = useState(initialBoard);
    const [turn, setTurn] = useState(game.game.turn.username)
    const [error, setError] = useState<string | null>(null)
    const [currentOpening, setCurrentOpening] = useState<string | null>(null)

    useEffect(() => {
        const interval = setInterval(() => {
            if(user != turn){
                refreshGame().then(r => console.log("refreshing..."));
            }
        }, 2000);
        return () => clearInterval(interval);
    }, [board, turn]);


    async function playMove(row: number, column: number) {
        const modal = new GamePlayInputModel(ActionType.PlacePiece, row, column, null, null)
        const [err, res] = await ( GameServices.makeMove(game.params.lId, game.params.gId, modal))
        console.log(`New res : ${res}`)

        const newGame = new Game(res.properties as GameDetailsOutputModel)
        setBoard(newGame.board.grid)
        setTurn(newGame.turn.username)
    }

    async function refreshGame() {
        const res = await (GameServices.getGame(game.params.lId, game.params.gId))
        const refreshedGame = new Game(res.properties as GameDetailsOutputModel)
        game.setGame(refreshedGame)
        setBoard(refreshedGame.board.grid)
        setTurn(refreshedGame.turn.username)
    }


    function handlePiecePlaced(row: number, column: number) {
        console.log(game)
        console.log(`${user} ---> ${game.game.turn.username}`)
        if(user != game.game.turn.username){
            console.log(`Not your turn '${user}' ---> '${game.game.turn.username}'`) // Diaolog box no ecra
            return
        }

        if (board[row][column] === null) {
            playMove(row, column)
        }
    }

    return (
        <div>
            <h1>Gomoku Game</h1>
            <Typography variant="h6" gutterBottom>
                {`Turn: ${game.game.turn.username == user ? "Your turn" : game.game.turn.username}`}
            </Typography>
            <Typography variant="h6" gutterBottom>
                {`YOU: ${user}`}
            </Typography>
            <IconButton onClick={refreshGame}>Refresh
                <RefreshIcon/>
            </IconButton>

            <BoardView board={board} onPiecePlaced={handlePiecePlaced}/>
        </div>
    )
}