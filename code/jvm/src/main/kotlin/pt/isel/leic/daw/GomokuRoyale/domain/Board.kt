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

    private fun checkWin(piece: Piece, position: Position): Boolean =
            checkVerticalWin(piece, position) ||
                    checkHorizontalWin(piece, position) ||
                    checkBackSlashWin(piece, position) ||
                    checkSlashWin(piece, position)

    private fun checkVerticalWin(piece: Piece, position: Position): Boolean {
        var winningPieces = 0
        val minVertical = max(0, (position.line - winningLength + 1)) //3
        val maxVertical = min(boardSize - 1, position.line + winningLength - 1) //11

        for (i in minVertical..maxVertical) {
            winningPieces = if (board[i][position.column] == piece) winningPieces + 1
            else 0
        }

        return if (overflowAllowed) winningPieces >= winningLength else winningPieces == winningLength
    }

    private fun checkHorizontalWin(piece: Piece, position: Position): Boolean {

        return true
    }

    private fun checkBackSlashWin(piece: Piece, position: Position): Boolean {

        return true
    }

    private fun checkSlashWin(piece: Piece, position: Position): Boolean {

        return true
    }
}