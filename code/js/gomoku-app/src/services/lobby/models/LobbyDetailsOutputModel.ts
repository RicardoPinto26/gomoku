import {User} from "../../../domain/User";

export interface LobbyDetailsOutputModel {
    id: number;
    user1: User;
    user2: User | null;
    gridSize: number;
    opening: string;
    winningLength: number;
    pointsMargin: number;
    overflow: boolean;
}