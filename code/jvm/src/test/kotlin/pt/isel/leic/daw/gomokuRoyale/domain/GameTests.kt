package pt.isel.leic.daw.gomokuRoyale.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.BoardWrongType
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserNotInGame
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import kotlin.test.Test
import kotlin.test.assertIs

class GameTests {
    private lateinit var user1: User
    private lateinit var user2: User
    private lateinit var settings: GameSettings
    private lateinit var game: Game

    @BeforeEach
    fun setup() {
        user1 = User(1, "user1", "user1@email.com", "password1")
        user2 = User(2, "user2", "user2@email.com", "password2")
        settings = GameSettings(boardSize = 10, winningLength = 5, opening = Opening.FREESTYLE, overflowAllowed = false)
        game = Game("game1", user1, user2, settings)
    }

    @Test
    fun `placePiece should update board and user ratings correctly`() {
        val newGame = game.placePiece(Piece.WHITE, Position(0, 0), user1)
        assertIs<BoardRun>(newGame.board)
        assertEquals(user1.rating, newGame.user1.rating) // user1's rating should not change
        assertEquals(user2.rating, newGame.user2.rating) // user2's rating should not change
    }

    @Test
    fun `placePiece should throw exception if user not in game`() {
        val otherUser = User(3, "user3", "user3@email.com", "password3")
        assertThrows<UserNotInGame> {
            game.placePiece(Piece.WHITE, Position(0, 0), otherUser)
        }
    }

    @Test
    fun `placePiece should throw exception if wrong board type`() {
        val finishedGame = game.copy(board = BoardWin(listOf(listOf(null)), BlackPlayer(user1)))
        assertThrows<BoardWrongType> {
            finishedGame.placePiece(Piece.WHITE, Position(0, 0), user1)
        }
    }

    @Test
    fun `forfeitGame should update board and user ratings correctly`() {
        val newGame = game.forfeitGame()
        assertIs<BoardWin>(newGame.board)
    }

    @Test
    fun `checkGameEnd should return false for ongoing game`() {
        assertFalse(game.checkGameEnd())
    }

    @Test
    fun `checkGameEnd should return true for finished game`() {
        val finishedGame = game.copy(board = BoardWin(listOf(listOf(null)), BlackPlayer(user1)))
        assertTrue(finishedGame.checkGameEnd())
    }

    @Test
    fun `checkUserInGame should return user if in game`() {
        assertEquals(user1, game.checkUserInGame(user1.id))
        assertEquals(user2, game.checkUserInGame(user2.id))
    }

    @Test
    fun `checkUserInGame should return null if user not in game`() {
        assertNull(game.checkUserInGame(3))
    }
}
