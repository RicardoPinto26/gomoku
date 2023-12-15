export enum OpeningMove {
    PLACE_WHITE= 'PLACE_WHITE',
    PLACE_BLACK = 'PLACE_BLACK',
    CHOOSE_COLOR = 'CHOOSE_COLOR',
    CHANGE_PLAYER = 'CHANGE_PLAYER',
    CHOOSE_NEXT_MOVE = 'CHOOSE_NEXT_MOVE',
    SWAP_COLOR = 'SWAP_COLOR'
}

export namespace OpeningMove {
    export function placeColor(piece: Piece): OpeningMove {
        switch (piece) {
            case Piece.BLACK:
                return OpeningMove.PLACE_BLACK;
            case Piece.WHITE:
                return OpeningMove.PLACE_WHITE;
        }
    }
}

export enum Piece {
    BLACK = 'BLACK',
    WHITE = 'WHITE'
}

export class Opening {
    movesList: OpeningMove[];
    variantList: Opening[];

    constructor(movesList: OpeningMove[], variantList: Opening[] = []) {
        this.movesList = movesList;
        this.variantList = variantList;
    }

    static FREESTYLE = new Opening([], []);
    static PRO = new Opening([
        OpeningMove.PLACE_BLACK,
        OpeningMove.CHANGE_PLAYER,
        OpeningMove.PLACE_WHITE,
        OpeningMove.CHANGE_PLAYER,
        OpeningMove.PLACE_BLACK
    ]);
    static LONG_PRO = new Opening([
        OpeningMove.PLACE_BLACK,
        OpeningMove.CHANGE_PLAYER,
        OpeningMove.PLACE_WHITE,
        OpeningMove.CHANGE_PLAYER,
        OpeningMove.PLACE_BLACK,
        OpeningMove.CHANGE_PLAYER
    ]);
    static SWAP = new Opening([
        OpeningMove.PLACE_BLACK,
        OpeningMove.PLACE_BLACK,
        OpeningMove.PLACE_WHITE,
        OpeningMove.CHANGE_PLAYER,
        OpeningMove.CHOOSE_COLOR
    ]);
    static SWAP2_1 = new Opening([
        OpeningMove.PLACE_WHITE,
        OpeningMove.CHANGE_PLAYER
    ]);
    static SWAP2_2 = new Opening([
        OpeningMove.SWAP_COLOR
    ]);
    static SWAP2_3 = new Opening([
        OpeningMove.PLACE_BLACK,
        OpeningMove.PLACE_WHITE,
        OpeningMove.CHANGE_PLAYER,
        OpeningMove.CHOOSE_COLOR
    ]);
    static SWAP2 = new Opening([
        OpeningMove.PLACE_BLACK,
        OpeningMove.PLACE_BLACK,
        OpeningMove.PLACE_WHITE,
        OpeningMove.CHANGE_PLAYER,
        OpeningMove.CHOOSE_NEXT_MOVE
    ], [Opening.SWAP2_1, Opening.SWAP2_2, Opening.SWAP2_3]);
}

