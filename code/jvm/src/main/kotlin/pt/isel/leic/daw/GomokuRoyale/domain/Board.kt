package pt.isel.leic.daw.GomokuRoyale.domain

import kotlin.math.min
import kotlin.math.max

data class Board(
    private val boardSize: Int,
    private val winningLength: Int,
    private val overflowAllowed: Boolean,
    private val opening: Opening
) {
    private val board = MutableList(boardSize) { MutableList<Piece?>(boardSize) { null } }

    private var openingFinished =
        when (opening) {
            Opening.FREESTYLE -> true
        }

    fun makeMove(piece: Piece, position: Position): Boolean {
        require(board[position.line][position.column] == null)

        board[position.line][position.column] = piece
        return checkWin(piece, position)
    }

    private fun checkWin(piece: Piece, position: Position): Boolean {
        val directions =
            arrayOf(
                Pair(-1, -1), // Down Left
                Pair(-1, 1),  // Ttop Left
                Pair(1, -1),  // Down Right
                Pair(1, 1),   // Top Right
                Pair(1, 0),   // Left Horizontal
                Pair(-1, 0),  // Right Horizontal
                Pair(0, -1),  // Down Vertical
                Pair(0, 1)    // Top Vertocal
            )

        for (direction in directions) {
            var winningPieces = 0
            var row = position.line
            var column = position.column

            while (row in 0 until boardSize && column in 0 until boardSize) {
                winningPieces = if (board[row][column] == piece) winningPieces + 1 else 0

                if (winningPieces == winningLength) return true

                row += direction.first
                column += direction.second
            }
        }

        return false
    }
}