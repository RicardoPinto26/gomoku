import {Board, BoardCell, Position} from "./Board";


export function checkWin(board: Array<Array<BoardCell>>, piece: BoardCell, position: Position, winningLength: number, overflowAllowed: boolean): boolean {
    return checkVerticalWin(board, piece, position, winningLength, overflowAllowed) ||
        checkHorizontalWin(board, piece, position, winningLength, overflowAllowed) ||
        checkDiagonalWin(board, piece, position, winningLength, overflowAllowed);
}

function checkVerticalWin(board: Array<Array<BoardCell>>, piece: BoardCell, position: Position, winningLength: number, overflowAllowed: boolean): boolean {
    let winningPieces = 0;
    const minVertical = Math.max(0, position.row - winningLength + 1);
    const maxVertical = Math.min(board.length - 1, position.row + winningLength - 1);

    for (let i = minVertical; i <= maxVertical; i++) {
        if (board[i][position.column] === piece) {
            winningPieces++;
            if (winningPieces === winningLength) return true;
        } else {
            winningPieces = 0; // Reset count if sequence is broken
        }
    }
    return false;
}

function checkHorizontalWin(board: Array<Array<BoardCell>>, piece: BoardCell, position: Position, winningLength: number, overflowAllowed: boolean): boolean {
    let winningPieces = 0;
    const minHorizontal = Math.max(0, position.column - winningLength + 1);
    const maxHorizontal = Math.min(board.length - 1, position.column + winningLength - 1);

    for (let i = minHorizontal; i <= maxHorizontal; i++) {
        if (board[position.row][i] === piece) {
            winningPieces++;
            if (winningPieces === winningLength) return true;
        } else {
            winningPieces = 0;
        }
    }
    return false;
}

function checkDiagonalWin(board: Array<Array<BoardCell>>, piece: BoardCell, position: Position, winningLength: number, overflowAllowed: boolean): boolean {
    // Check for diagonal wins in both directions
    return checkDiagonal(board, piece, position, 1, winningLength, overflowAllowed) || checkDiagonal(board, piece, position, -1, winningLength, overflowAllowed);
}

function checkDiagonal(board: Array<Array<BoardCell>>, piece: BoardCell, position: Position, direction: number, winningLength: number, overflowAllowed: boolean): boolean {
    let winningPieces = 0;

    for (let i = winningLength + 1; i < winningLength; i++) {
        const currentRow = position.row + i;
        const currentColumn = position.column + i * direction;

        if (currentRow >= 0 && currentRow < board.length &&
            currentColumn >= 0 && currentColumn < board.length &&
            board[currentRow][currentColumn] === piece) {
            winningPieces++;
            if (winningPieces === winningLength) return true;
        } else {
            winningPieces = 0; // Reset count if sequence is broken
        }
    }

    return false;
}

