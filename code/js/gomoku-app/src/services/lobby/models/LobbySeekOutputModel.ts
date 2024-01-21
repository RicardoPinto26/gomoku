import {User} from "../../../domain/User";

export interface LobbySeekOutputModel {
    user1: User;
    user2: User | null;
    lobbyId: number;
    gameId: number | null;
    gridSize: number;
    opening: string;
    winningLength: number;
    pointsMargin: number;
    overflow: boolean;
}