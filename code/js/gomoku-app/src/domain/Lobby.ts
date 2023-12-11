import {User} from "./User";

export interface Lobby {
    id: number;
    user1: User;
    user2: User | null;
    gridSize: number;
    opening: string;
    winningLength: number;
    pointsMargin: number;
    overflow: boolean;
    gameId?: number
}

export class Lobby {
    constructor(
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
    }
}