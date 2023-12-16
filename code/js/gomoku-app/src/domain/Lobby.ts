import {User} from "./User";
import {GameDetailsOutputModel} from "../services/game/models/GameDetailsOutputModel";
import {Board, convertJsonToBoard} from "./game/Board";
import {defaultSettings} from "../components/game/matchmake/GameSettings";
import {LobbyDetailsOutputModel} from "../services/lobby/models/LobbyDetailsOutputModel";

export interface Lobby {
    id: number;
    user1: User;
    user2: User | null;
    gridSize: number;
    opening: string;
    winningLength: number;
    pointsMargin: number;
    overflow: boolean;
    gameId?: number | null;
}

export class Lobby {
    /*constructor(
        id: number,
        user1: User,
        user2: User | null,
        gridSize: number,
        opening: string,
        overflow: boolean,
        pointsMargin: number,
        winningLength: number,
        gameId?: number
    ) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.gridSize = gridSize;
        this.opening = opening;
        this.overflow = overflow;
        this.pointsMargin = pointsMargin;
        this.winningLength = winningLength;
        this.gameId = gameId;
    }*/
    constructor(lobbyDetails: LobbyDetailsOutputModel) {
        this.id = lobbyDetails.id;
        this.user1 = lobbyDetails.user1;
        this.user2 = lobbyDetails.user2;
        this.gridSize = lobbyDetails.gridSize;
        this.opening = lobbyDetails.opening;
        this.overflow = lobbyDetails.overflow;
        this.pointsMargin = lobbyDetails.pointsMargin;
        this.winningLength = lobbyDetails.winningLength;
        this.gameId = null;
    }
}
