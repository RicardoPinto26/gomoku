package pt.isel.leic.daw.GomokuRoyale.domain

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class BoardTests {
    @Test
    fun `Winning Vertical Move returns true`() {
        val board = Board(10, 5, false)
        assertFalse(board.placePiece(Piece.WHITE, Position(3, 2)))
        assertFalse(board.placePiece(Piece.WHITE, Position(4, 2)))
        assertFalse(board.placePiece(Piece.WHITE, Position(0, 2)))
        assertFalse(board.placePiece(Piece.WHITE, Position(1, 2)))
        assertTrue(board.placePiece(Piece.WHITE, Position(2, 2)))
    }

    @Test
    fun `Winning Horizontal Move returns true`() {
        val board = Board(10, 5, false)
        assertFalse(board.placePiece(Piece.WHITE, Position(2, 3)))
        assertFalse(board.placePiece(Piece.WHITE, Position(2, 4)))
        assertFalse(board.placePiece(Piece.WHITE, Position(2, 0)))
        assertFalse(board.placePiece(Piece.WHITE, Position(2, 1)))
        assertTrue(board.placePiece(Piece.WHITE, Position(2, 2)))
    }

    @Test
    fun `Winning Backslash Move returns true`() {
        val board = Board(10, 5, false)
        assertFalse(board.placePiece(Piece.WHITE, Position(4, 4)))
        assertFalse(board.placePiece(Piece.WHITE, Position(2, 2)))
        assertFalse(board.placePiece(Piece.WHITE, Position(5, 5)))
        assertFalse(board.placePiece(Piece.WHITE, Position(6, 6)))
        assertTrue(board.placePiece(Piece.WHITE, Position(3, 3)))
    }

    @Test
    fun `Winning Slash Move returns true`() {
        val board = Board(10, 5, false)
        assertFalse(board.placePiece(Piece.WHITE, Position(5, 4)))
        assertFalse(board.placePiece(Piece.WHITE, Position(6, 3)))
        assertFalse(board.placePiece(Piece.WHITE, Position(3, 6)))
        assertFalse(board.placePiece(Piece.WHITE, Position(2, 7)))
        assertTrue(board.placePiece(Piece.WHITE, Position(4, 5)))
    }
}