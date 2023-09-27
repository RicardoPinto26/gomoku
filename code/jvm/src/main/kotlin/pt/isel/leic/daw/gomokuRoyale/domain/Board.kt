package pt.isel.leic.daw.gomokuRoyale.domain

import kotlin.math.max
import kotlin.math.min

sealed interface Board {
    val internalBoard: List<List<Piece?>>

    fun placePiece(piece: Piece, position: Position): Board
}

class BoardWin internal constructor(
    override val internalBoard: List<List<Piece?>>
) : Board {
    override fun placePiece(piece: Piece, position: Position): Board {
        throw IllegalStateException("This game has already finished with a win.")
    }
}

class BoardDraw internal constructor(
    override val internalBoard: List<List<Piece?>>
) : Board {
    override fun placePiece(piece: Piece, position: Position): Board {
        throw IllegalStateException("This game has already finished with a draw.")
    }
}

class BoardRun internal constructor(
    private val winningLength: Int,
    private val overflowAllowed: Boolean,
    override val internalBoard: List<List<Piece?>>
) : Board {

    constructor(
        boardSize: Int,
        winningLength: Int,
        overflowAllowed: Boolean
    ) : this(winningLength, overflowAllowed, List(boardSize) { List(boardSize) { null } })

    override fun placePiece(piece: Piece, position: Position): Board {
        require(internalBoard[position.row][position.column] == null)

        val newBoard = internalBoard.mapIndexed { row, list ->
            if(row == position.row)
                list.mapIndexed { column, elem ->
                    if(column == position.column)
                        piece
                    else elem
                }
            else list
        }
        return when {
            checkWin(newBoard, piece, position) -> BoardWin(newBoard)
            newBoard.all { it.all { piece -> piece != null } } -> BoardDraw(newBoard)
            else -> BoardRun(winningLength, overflowAllowed, newBoard)
        }

    }

    private fun checkWin(board: List<List<Piece?>>, piece: Piece, position: Position): Boolean =
        checkVerticalWin(board, piece, position) ||
                checkHorizontalWin(board, piece, position) ||
                checkSlashAndBackslashWin(board, piece, position)

    private fun checkVerticalWin(board: List<List<Piece?>>, piece: Piece, position: Position): Boolean {
        var winningPieces = 0
        val minVertical = max(0, (position.row - winningLength + 1))
        val maxVertical = min(board.size - 1, position.row + winningLength - 1)

        for (i in minVertical..maxVertical) {
            if (board[i][position.column] == piece) winningPieces++
            else {
                if (overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength) return true
            }
        }
        return overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength
    }

    private fun checkHorizontalWin(board: List<List<Piece?>>, piece: Piece, position: Position): Boolean {
        var winningPieces = 0
        val minHorizontal = max(0, (position.column - winningLength + 1))
        val maxHorizontal = min(board.size - 1, position.column + winningLength - 1)

        for (i in minHorizontal..maxHorizontal) {
            if (board[position.row][i] == piece) winningPieces++
            else {
                if (overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength) return true
            }
        }
        return overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength
    }

    private fun checkSlashAndBackslashWin(board: List<List<Piece?>>, piece: Piece, position: Position): Boolean {
        var winningPieces = 0
        val min = max(0, max(position.column - winningLength + 1, position.row - winningLength + 1))
        val max = min(board.size - 1, min(position.column + winningLength - 1, position.row + winningLength - 1))
        for(direction in -1..1 step 2) {
            var column = if (direction == 1) min else max
            for (row in min..max) {
                if (board[row][column] == piece) winningPieces++
                else {
                    if (overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength) return true
                }
                column += direction
            }
            if(overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength) return true
        }
        return false
    }
}