package pt.isel.leic.daw.gomokuRoyale.domain

import kotlin.test.Test
import kotlin.test.assertIs

class BoardTests {
    @Test
    fun `Winning Vertical Move returns true`() {
        val board = BoardRun(10, 5, false)
            .placePiece(Piece.WHITE, Position(3, 2))
            .placePiece(Piece.WHITE, Position(4, 2))
            .placePiece(Piece.WHITE, Position(0, 2))
            .placePiece(Piece.WHITE, Position(1, 2))
            .placePiece(Piece.WHITE, Position(2, 2))

        assertIs<BoardWin>(board)
    }


    @Test
    fun `Winning Horizontal Move returns true`() {
        val board = BoardRun(10, 5, false)
        .placePiece(Piece.WHITE, Position(2, 3))
        .placePiece(Piece.WHITE, Position(2, 4))
        .placePiece(Piece.WHITE, Position(2, 0))
        .placePiece(Piece.WHITE, Position(2, 1))
        .placePiece(Piece.WHITE, Position(2, 2))

        assertIs<BoardWin>(board)
    }

    @Test
    fun `Winning Backslash Move returns true`() {
        val board = BoardRun(10, 5, false)
        .placePiece(Piece.WHITE, Position(4, 4))
        .placePiece(Piece.WHITE, Position(2, 2))
        .placePiece(Piece.WHITE, Position(5, 5))
        .placePiece(Piece.WHITE, Position(6, 6))
        .placePiece(Piece.WHITE, Position(3, 3))

        assertIs<BoardWin>(board)
    }

    @Test
    fun `Winning Slash Move returns true`() {
        val board = BoardRun(10, 5, false)
        .placePiece(Piece.WHITE, Position(5, 4))
        .placePiece(Piece.WHITE, Position(6, 3))
        .placePiece(Piece.WHITE, Position(3, 6))
        .placePiece(Piece.WHITE, Position(2, 7))
        .placePiece(Piece.WHITE, Position(4, 5))

        assertIs<BoardWin>(board)
    }

    @Test
    fun `Overflow win when its not allowed does not return true`() {
        val board = BoardRun(10, 5, false)
        .placePiece(Piece.WHITE, Position(3, 2))
        .placePiece(Piece.WHITE, Position(4, 2))
        .placePiece(Piece.WHITE, Position(0, 2))
        .placePiece(Piece.WHITE, Position(1, 2))
        .placePiece(Piece.WHITE, Position(5, 2))
        .placePiece(Piece.WHITE, Position(2, 2))

        assertIs<BoardRun>(board)
    }

    @Test
    fun `Overflow win when it is allowed does returns true`() {
        val board = BoardRun(10, 5, true)
            .placePiece(Piece.WHITE, Position(3, 2))
            .placePiece(Piece.WHITE, Position(4, 2))
            .placePiece(Piece.WHITE, Position(0, 2))
            .placePiece(Piece.WHITE, Position(1, 2))
            .placePiece(Piece.WHITE, Position(5, 2))
            .placePiece(Piece.WHITE, Position(2, 2))

        assertIs<BoardWin>(board)
    }
}