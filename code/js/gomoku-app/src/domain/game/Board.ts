
type BoardCell = 'BLACK' | 'WHITE' | null;
export class Board {
    private size: number;
    grid: Array<Array<BoardCell>>;

    constructor(size: number, grid: Array<Array<BoardCell>>) {
        this.size = size;
        this.grid = grid;
    }


}

export function  initializeBoard(size: number): Array<Array<BoardCell>> {
    return Array<Array<BoardCell>>(size).fill(Array<BoardCell>(size).fill(null));
}
export function convertJsonToBoard(json: string): Array<Array<BoardCell>> {
    const parsedArray: Array<Array<BoardCell>> = JSON.parse(json);

    console.log(parsedArray)

    return parsedArray.map(row => row.map(cell => {
        if (cell === 'BLACK' || cell === 'WHITE' || cell === null) {
            return cell;
        } else {
            throw new Error('Invalid cell value');
        }
    }));
}