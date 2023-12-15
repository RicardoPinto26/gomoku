import {Board, convertJsonToBoard} from "./Board";
import {defaultSettings, GameSettings} from "../../components/game/matchmake/GameSettings";
import {User} from "../User";
import {State} from "./Cell";

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
    private config: GameSettings = defaultSettings
    constructor(gameDetails: GameDetailsOutputModel) {
        this.id = gameDetails.id;
        this.blackPlayer = gameDetails.blackPlayer;
        this.whitePlayer = gameDetails.whitePlayer;
        this.board = new Board(defaultSettings.gridSize, convertJsonToBoard(gameDetails.board))
        this.turn = gameDetails.turn;
        this.openingIndex = gameDetails.openingIndex;
        this.openingVariant = gameDetails.openingVariant;
        this.state = gameDetails.state;
        this.winner = gameDetails.winner;
        this.config = defaultSettings; // Hardcoded
    }

}


export interface GameDetailsOutputModel {
    id: number;
    blackPlayer: User;
    whitePlayer: User;
    board: string;
    turn: User;
    openingIndex: number;
    openingVariant: string | null;
    state: string;
    winner: User | null;
}