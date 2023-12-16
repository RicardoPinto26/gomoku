import {User} from "../../../domain/User";

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