import React, {useEffect, useState} from 'react';
import {BoardView} from './BoardView';
import {Game} from "../../../domain/game/Game";
import {GameServices} from "../../../services/game/GameServices";
import {ActionType, GamePlayInputModel} from "../../../services/game/models/GamePlayInputModel";
import {useCurrentUser} from "../../../utils/Authn";
import {initializeBoard} from "../../../domain/game/Board";
import {handleRequest} from "../../../services/utils/fetchSiren";
import {handleError} from "../../../services/utils/errorUtils";
import {useNavigate} from "react-router-dom";
import {GameBarStatus} from "./GameBarStatus";
import {Opening, OpeningMove} from "../../../domain/game/Opening";
import {NextMoveDialog} from "./utils/NextMoveDialog";
import {ChooseColorDialog} from "./utils/ChooseColorDialog";
import {ForfeitButton} from "./utils/ForfeitGame";
import {WinnerDialog} from "./utils/WinnerDialog";

interface GameProps {
    game: Game
    params: { lId: number, gId: number }
    setGame: (game: Game) => void
}

export function GameBoard(game: GameProps) {
    const navigate = useNavigate()
    const user = useCurrentUser()

    const boardSize = game.game.board.grid.length;
    const initialBoard = initializeBoard(boardSize)
    const [board, setBoard] = useState(initialBoard);
    const [turn, setTurn] = useState(game.game.turn.username)
    const [winner, setWinner] = useState<string | null>(null)
    const [error, setError] = useState<string | null>(null)

    const [lobbyOpening, setLobbyOpening] = useState<string>(game.game.config.opening)
    const [currentMoveType, setCurrentMoveType] = useState<OpeningMove | null>(null)
    const [currentOpeningIndex, setCurrentOpeningIndex] = useState<number>(game.game.openingIndex)


    const [choosedOpening, setChoosedOpening] = useState<string | null>(game.game.openingVariant)
    const [choosedColor, setChoosedColor] = useState<string | null>(null)

    const [isMoveDialogOpen, setIsMoveDialogOpen] = useState(false);
    const [isColorDialogOpen, setIsColorDialogOpen] = useState(false);
    const [isWinnerDialogOpen, setIsWinnerDialogOpen] = useState(false);


    useEffect(() => {
        handleTurn(user!, turn, choosedOpening, lobbyOpening, currentOpeningIndex)

        const interval = setInterval(() => {
            if (user != turn) {
                refreshGame()
                    .then(r => {
                        console.log("refreshing...")
                        if (winner) {
                            handleOpenWinnerDialog()
                        }
                    })
            }
        }, 2000);
        return () => clearInterval(interval)
    }, [board, turn])


    async function chooseNextMove(move: Opening) {
        const openingName = Opening.toName(move)
        setChoosedOpening(openingName)
        handleCloseMoveDialog();
        console.log(`Choosed opening: ${openingName}`)
        const modal = new GamePlayInputModel(ActionType.ChooseMove, null, null, openingName, null)
        const [error, res] = await handleRequest(GameServices.play(game.params.lId, game.params.gId, modal))

        if (error) {
            handleError(error, setError, navigate)
            return
        }
        console.log(res)
        const newGame = new Game(res.properties!, game.game.config)
        game.setGame(newGame)
        setTurn(newGame.turn.username)
    }


    async function chooseColor(color: string) {
        handleCloseColorDialog()
        setChoosedColor(color)
        const modal = new GamePlayInputModel(ActionType.ChooseColor, null, null, null, color)
        const [error, res] = await handleRequest(GameServices.play(game.params.lId, game.params.gId, modal))

        if (error) {
            handleError(error, setError, navigate)
            return
        }
        console.log(res)
        const newGame = new Game(res.properties!, game.game.config)
        game.setGame(newGame)
        setTurn(newGame.turn.username)
        setCurrentOpeningIndex(newGame.openingIndex)
    }

    async function playMove(row: number, column: number) {
        const modal = new GamePlayInputModel(ActionType.PlacePiece, row, column, null, null)
        const [error, res] = await handleRequest(GameServices.play(game.params.lId, game.params.gId, modal))
        if (error) {
            handleError(error, setError, navigate)
            return null
        }
        console.log(res)
        const newGame = new Game(res.properties!, game.game.config)
        setBoard(newGame.board.grid)
        setTurn(newGame.turn.username)
        setCurrentOpeningIndex(newGame.openingIndex)
        setWinner(newGame.winner ? newGame.winner.username : null)
        return newGame.winner ? newGame.winner.username : null
    }

    async function refreshGame() {
        const [error, res] = await handleRequest((GameServices.getGame(game.params.lId, game.params.gId)))
        if (error) {
            handleError(error, setError, navigate)
            return
        }
        const refreshedGame = new Game(res.properties!, game.game.config)
        setBoard(refreshedGame.board.grid)
        setTurn(refreshedGame.turn.username)
        setCurrentOpeningIndex(refreshedGame.openingIndex)
        setChoosedOpening(refreshedGame.openingVariant)
        setWinner(refreshedGame.winner ? refreshedGame.winner.username : null)
    }


    function handlePiecePlaced(row: number, column: number) {
        if (user != turn) {
            console.log(`Not your turn '${user}' ---> '${turn}'`) // Diaolog box no ecra
            return
        }

        if (board[row][column] === null) {
            playMove(row, column).then(r => {
                if (r != null) {
                    handleOpenWinnerDialog()
                }
            })
        }
    }

    function handleTurn(user: string, turn: string, choosedOpening: string | null, lobbyOpening: string, currentOpeningIndex: number) {
        if (user == turn) {
            const newMoveType = Opening.getCurrentMoveType(Opening.from(choosedOpening ? choosedOpening : lobbyOpening!)!, currentOpeningIndex)
            console.log(`New move type: ${newMoveType} - index: ${currentOpeningIndex}; movesList: ${Opening.from(lobbyOpening)!.movesList}}`)
            switch (newMoveType) {
                case OpeningMove.CHOOSE_NEXT_MOVE:
                    console.log("Choose next move type")
                    handleOpenMoveDialog()
                    break
                case OpeningMove.CHOOSE_COLOR:
                    console.log("Choose next color type")
                    handleOpenColorDialog()
                    break
            }
            setCurrentMoveType(newMoveType)
        }
    }

    // Dialogs

    const handleOpenMoveDialog = () => {
        setIsMoveDialogOpen(true);
    };
    const handleCloseMoveDialog = () => {
        setIsMoveDialogOpen(false);
    };
    const handleOpenColorDialog = () => {
        setIsColorDialogOpen(true);
    };
    const handleCloseColorDialog = () => {
        setIsColorDialogOpen(false);
    };

    const handleOpenWinnerDialog = () => {
        setIsWinnerDialogOpen(true);
    };
    const handleCloseWinnerDialog = () => {
        setIsWinnerDialogOpen(false);
    };


    return (
        <div>
            <h1>Gomoku Game</h1>
            <GameBarStatus
                currentPlayer={turn}
                gameState={choosedOpening ? choosedOpening : lobbyOpening}
                player={user!} onRefresh={refreshGame}/>

            <NextMoveDialog
                opening={Opening.from(lobbyOpening)!}
                open={isMoveDialogOpen}
                onClose={handleCloseMoveDialog}
                onMoveSelect={chooseNextMove}
            />

            <ChooseColorDialog
                open={isColorDialogOpen}
                onClose={handleCloseMoveDialog}
                onColorSelected={chooseColor}
            />

            <WinnerDialog
                open={isWinnerDialogOpen}
                onClose={handleCloseWinnerDialog}
                onReturnToMenu={() => navigate("/")}
                winner={winner == user}
            />


            <BoardView board={board} onPiecePlaced={handlePiecePlaced}/>
            <ForfeitButton gameId={game.params.gId} lobbyId={game.params.lId} isYourTurn={turn == user}/>
        </div>
    )
}


