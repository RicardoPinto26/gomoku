
export type BoardCell = 'BLACK' | 'WHITE' | null;
export class Board {
    size: number;
    grid: Array<Array<BoardCell>>;

    constructor(size: number, grid: Array<Array<BoardCell>>) {
        this.size = size;
        this.grid = grid;
    }

}

export class Position {
    row: number;
    column: number;

    constructor(row: number, column: number) {
        this.row = row;
        this.column = column;
    }
}

export function  initializeBoard(size: number): Array<Array<BoardCell>> {
    return Array<Array<BoardCell>>(size).fill(Array<BoardCell>(size).fill(null));
}
export function convertJsonToBoard(json: string): Array<Array<BoardCell>> {
    const parsedArray: Array<Array<BoardCell>> = JSON.parse(json);

    return parsedArray.map(row => row.map(cell => {
        if (cell === 'BLACK' || cell === 'WHITE' || cell === null) {
            return cell;
        } else {
            throw new Error('Invalid cell value');
        }
    }));
}