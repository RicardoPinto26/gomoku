import React, { useState } from 'react';
import {BoardView} from './BoardView';
import {Game} from "../../../domain/game/Game";
import {GameServices} from "../../../services/game/GameServices";
import {ActionType, GamePlayInputModel} from "../../../services/game/modals/GamePlayInputModel";

interface GameProps {
    game: Game;
    params: { lId: number, gId: number }
}

export function GameBoard(game: GameProps) {
    const boardSize = game.game.board.grid.length;
    const initialBoard = Array(boardSize).fill(null).map(() => Array(boardSize).fill(null));
    const [board, setBoard] = useState(initialBoard);
    const [counter, setCounter] = useState(0);


    async function playMove(row: number, column: number) {
        const modal = new GamePlayInputModel(ActionType.PlacePiece, row, column, null, null)
        const res = await ( GameServices.makeMove(game.params.lId, game.params.gId, modal))

        console.log(res)
    }
    function handlePiecePlaced(row: number, column: number) {
        const newPiece = (counter) % 2 === 0 ? 'BLACK' : 'WHITE';
        setCounter(counter + 1); // tests


        playMove(row, column)

        if (board[row][column] === null) {
            const newBoard = board.map((r, rowIndex) =>
                rowIndex === row ? r.map((c, columnIndex) =>
                    columnIndex === column ? newPiece : c
                ) : r
            );
            setBoard(newBoard);
        }
    }

    return (
        <div>
            <h1>Gomoku Game</h1>
            <BoardView board={board} onPiecePlaced={handlePiecePlaced}/>
        </div>
    )
}