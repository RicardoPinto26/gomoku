import {GameSettings} from "../../../components/game/matchmake/GameSettings";

export interface CreateLobbyInputModel {
    name: string,
    gridSize: number;
    winningLength: number;
    opening: string;
    pointsMargin: number;
    overflow: boolean;
}

export class CreateLobbyInputModel {
    constructor(name: string, settings: GameSettings) {
        this.name = name
        this.gridSize = settings.gridSize
        this.winningLength = settings.winningLength
        this.opening = settings.opening
        this.pointsMargin = settings.pointsMargin
        this.overflow = settings.overflow
    }
}