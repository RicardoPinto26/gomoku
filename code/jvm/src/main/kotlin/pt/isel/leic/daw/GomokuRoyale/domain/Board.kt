package pt.isel.leic.daw.GomokuRoyale.domain

import kotlin.math.max
import kotlin.math.min

data class Board(
    private val boardSize: Int,
    private val winningLength: Int,
    private val overflowAllowed: Boolean
) {
    private val board = MutableList(boardSize) { MutableList<Piece?>(boardSize) { null } }

    fun placePiece(piece: Piece, position: Position): Boolean {
        require(board[position.row][position.column] == null)

        board[position.row][position.column] = piece
        return checkWin(piece, position)
    }

    private fun checkWin(piece: Piece, position: Position): Boolean =
        checkVerticalWin(piece, position) ||
                checkHorizontalWin(piece, position) ||
                checkBackslashWin(piece, position) ||
                checkSlashWin(piece, position)

    private fun checkVerticalWin(piece: Piece, position: Position): Boolean {
        var winningPieces = 0
        val minVertical = max(0, (position.row - winningLength + 1))
        val maxVertical = min(boardSize - 1, position.row + winningLength - 1)

        for (i in minVertical..maxVertical) {
            if (board[i][position.column] == piece) winningPieces++
            else {
                if (overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength) return true
            }
        }
        println(winningPieces)
        return overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength
    }

    private fun checkHorizontalWin(piece: Piece, position: Position): Boolean {
        var winningPieces = 0
        val minHorizontal = max(0, (position.column - winningLength + 1))
        val maxHorizontal = min(boardSize - 1, position.column + winningLength - 1)

        for (i in minHorizontal..maxHorizontal) {
            if (board[position.row][i] == piece) winningPieces++
            else {
                if (overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength) return true
            }
        }
        println(winningPieces)
        return overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength
    }

    // Backslash = \
    private fun checkBackslashWin(piece: Piece, position: Position): Boolean {
        var winningPieces = 0
        val min = max(0, max(position.column - winningLength + 1, position.row - winningLength + 1))
        val max = min(boardSize - 1, min(position.column + winningLength - 1, position.row + winningLength - 1))

        for (i in min..max) {
            if (board[i][i] == piece) winningPieces++
            else {
                if (overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength) return true
            }
        }
        println(winningPieces)
        return overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength
    }

    // Slash = /
    private fun checkSlashWin(piece: Piece, position: Position): Boolean {
        var winningPieces = 0
        val min = max(0, max(position.column - winningLength + 1, position.row - winningLength + 1))
        val max = min(boardSize - 1, min(position.column + winningLength - 1, position.row + winningLength - 1))

        var column = max
        for (row in min..max) {
            if (board[row][column] == piece) winningPieces++
            else {
                if (overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength) return true
            }

            column--
        }
        println(winningPieces)
        return overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength
    }
}