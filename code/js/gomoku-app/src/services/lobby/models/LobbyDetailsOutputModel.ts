import {User} from "../../../domain/User";

export interface LobbyDetailsOutputModel {
    id: number;
    name: string;
    user1: User;
    user2: User | null;
    gridSize: number;
    opening: string;
    winningLength: number;
    pointsMargin: number;
    overflow: boolean;
    gameId: number | null;
}