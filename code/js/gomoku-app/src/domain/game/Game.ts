import {Board} from "./Board";
import {defaultSettings, GameSettings} from "../../components/game/matchmake/GameSettings";
import {User} from "../User";
import {State} from "./Cell";

export class Game {
    private id: number
    private player1: User
    private player2: User
    private config: GameSettings
    board: Board
    openingIndex: number
    private state: State
    turn: User
    private winner: null | User

    constructor(
        id: number,
        board: Board,
        state: State,
        player1: User,
        player2: User,
        turn: User,
        winner: null | User,
        config: GameSettings = defaultSettings,
        currentOpeningIndex: number = 0
    ) {
        this.id = id
        this.config = defaultSettings //HARDCODED
        this.board = board
        this.openingIndex = currentOpeningIndex
        this.state = state
        this.player1 = player1
        this.player2 = player2
        this.turn = turn
        this.winner = winner
    }
}
