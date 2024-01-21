export interface LobbyCreateOutputModel {
    id: number;
    username: string;
    pointsMargin: number;
    gridSize: number;
    opening: string;
    winningLength: number;
    overflow: boolean;
}