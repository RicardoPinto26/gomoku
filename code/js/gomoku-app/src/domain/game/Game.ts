import {Board, convertJsonToBoard} from "./Board";
import {defaultSettings, GameSettings} from "../../components/game/matchmake/GameSettings";
import {User} from "../User";
import {State} from "./Cell";
import {GameDetailsOutputModel} from "../../services/game/models/GameDetailsOutputModel";
import {Lobby} from "../Lobby";

export class Game {
    private id: number
    private blackPlayer: User
    private whitePlayer: User
    board: Board
    turn: User
    openingIndex: number
    openingVariant: string | null
    private state: string
    private winner: null | User
    config: GameSettings = defaultSettings
    constructor(gameDetails: GameDetailsOutputModel, gameSettings: GameSettings) {
        this.id = gameDetails.id;
        this.blackPlayer = gameDetails.blackPlayer;
        this.whitePlayer = gameDetails.whitePlayer;
        this.board = new Board(defaultSettings.gridSize, convertJsonToBoard(gameDetails.board))
        this.turn = gameDetails.turn;
        this.openingIndex = gameDetails.openingIndex;
        this.openingVariant = gameDetails.openingVariant;
        this.state = gameDetails.state;
        this.winner = gameDetails.winner;
        this.config = gameSettings
    }

}


