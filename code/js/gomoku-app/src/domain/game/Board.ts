export class Board {
    private size: number;
    grid: Array<Array<'BLACK' | 'WHITE' | null>>;

    constructor(size: number, grid: Array<Array<'BLACK' | 'WHITE' | null>>) {
        this.size = size;
        this.grid = grid;
    }

    static convertJsonToBoard(json: string): Array<Array<'BLACK' | 'WHITE' | null>> {
        const data = JSON.parse(json);

        const board: Array<Array<'BLACK' | 'WHITE' | null>> = [];

        for (let i = 0; i < data.length; i++) {
            const row = data[i];
            const boardRow: Array<'BLACK' | 'WHITE' | null> = [];

            for (let j = 0; j < row.length; j++) {
                const cellData = row[j];

                boardRow.push(cellData);
            }

            board.push(boardRow);
        }

        return board;
    }
}