export interface GamePlayInputModel {
    actionType: ActionType; // "PlacePiece", "ChooseMove", or "ChooseColor"
    positionX: number | null; // used for PlacePiece_x
    positionY: number | null; // used for PlacePiece_y
    moveChoice: string | null; // used for ChooseMove
    color: string | null; // used for ChooseColor
}

export class GamePlayInputModel {
    constructor(actionType: ActionType, positionX: number, positionY: number, moveChoice: string | null, color: string | null) {
        this.actionType = actionType;
        this.positionX = positionX;
        this.positionY = positionY;
        this.moveChoice = moveChoice;
        this.color = color;
    }
}

export enum ActionType {
    PlacePiece = 'PlacePiece',
    ChooseMove = 'ChooseMove',
    ChooseColor = 'ChooseColor'
}