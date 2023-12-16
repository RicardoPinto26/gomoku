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

export enum Color {
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

    static OPENINGS = {
        'FREESTYLE': Opening.FREESTYLE,
        'PRO': Opening.PRO,
        'LONG_PRO': Opening.LONG_PRO,
        'SWAP': Opening.SWAP,
        'SWAP2_1': Opening.SWAP2_1,
        'SWAP2_2': Opening.SWAP2_2,
        'SWAP2_3': Opening.SWAP2_3,
        'SWAP2': Opening.SWAP2
    };

    static from(opening: string): Opening | null {
        const key = opening.toUpperCase();

        if (key in Opening.OPENINGS) {
            return Opening.OPENINGS[key as keyof typeof Opening.OPENINGS];
        }
        return null;
    }
    static toName(opening: Opening): string {
        switch (opening) {
            case Opening.FREESTYLE:
                return 'FREESTYLE';
            case Opening.PRO:
                return 'PRO';
            case Opening.LONG_PRO:
                return 'LONG_PRO';
            case Opening.SWAP:
                return 'SWAP';
            case Opening.SWAP2_1:
                return 'SWAP2_1';
            case Opening.SWAP2_2:
                return 'SWAP2_2';
            case Opening.SWAP2_3:
                return 'SWAP2_3';
            case Opening.SWAP2:
                return 'SWAP2';
        }
        return '';
    }
    static giveOpeningNames(opening: Opening): string {
        switch (opening) {
            case Opening.FREESTYLE:
                return 'FREESTYLE';
            case Opening.PRO:
                return 'PRO';
            case Opening.LONG_PRO:
                return 'LONG_PRO';
            case Opening.SWAP:
                return 'SWAP';
            case Opening.SWAP2_1:
                return 'Play as white and place a second white stone';
            case Opening.SWAP2_2:
                return 'Swap their color and choose to play as black';
            case Opening.SWAP2_3:
                return 'Place two stones (1W, 1B), and pass the choice of which color to the other player';
            case Opening.SWAP2:
                return 'SWAP2';
        }
        return '';
    }
    static getCurrentMoveType(opening: Opening, openingIndex: number): OpeningMove | null {
        return opening.movesList[openingIndex] || null;
    }
}

