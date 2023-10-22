package pt.isel.leic.daw.gomokuRoyale.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.*
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import kotlin.test.Test
import kotlin.test.assertIs

class BoardTests {

    private val user1 = User(1, "user1", "user1@email.com", "password1")
    private val user2 = User(2, "user2", "user2@email.com", "password2")

    private val player1 = createWhitePlayer(user1)
    private val player2 = createBlackPlayer(user2)

    private val position = Position(0, 0)
    private val piece = Piece.WHITE

    private var boardRun = BoardRun(10, 5, false, player1, player2, player1)
    private val boardWin = BoardWin(listOf(listOf(piece)), player1)
    private val boardDraw = BoardDraw(listOf(listOf(piece)))

    @Test
    fun `Winning Vertical Move returns true`() {
        val board = BoardRun(
            boardSize = 10,
            winningLength = 5,
            overflowAllowed = false,
            player1 = player1,
            player2 = player2,
            turn = player1
        )
            .placePiece(Piece.WHITE, Position(3, 2), player1.user)
            .placePiece(Piece.WHITE, Position(4, 2), player2.user)
            .placePiece(Piece.WHITE, Position(0, 2), player1.user)
            .placePiece(Piece.WHITE, Position(1, 2), player2.user)
            .placePiece(Piece.WHITE, Position(2, 2), player1.user)

        assertIs<BoardWin>(board)
    }

    @Test
    fun `Winning Horizontal Move returns true`() {
        val board = BoardRun(
            boardSize = 10,
            winningLength = 5,
            overflowAllowed = false,
            player1 = player1,
            player2 = player2,
            turn = player1
        )
            .placePiece(Piece.WHITE, Position(2, 3), player1.user)
            .placePiece(Piece.WHITE, Position(2, 4), player2.user)
            .placePiece(Piece.WHITE, Position(2, 0), player1.user)
            .placePiece(Piece.WHITE, Position(2, 1), player2.user)
            .placePiece(Piece.WHITE, Position(2, 2), player1.user)

        assertIs<BoardWin>(board)
    }

    @Test
    fun `Winning Backslash Move returns true`() {
        val board = BoardRun(
            boardSize = 10,
            winningLength = 5,
            overflowAllowed = false,
            player1 = player1,
            player2 = player2,
            turn = player1
        )
            .placePiece(Piece.WHITE, Position(4, 4), player1.user)
            .placePiece(Piece.WHITE, Position(2, 2), player2.user)
            .placePiece(Piece.WHITE, Position(5, 5), player1.user)
            .placePiece(Piece.WHITE, Position(6, 6), player2.user)
            .placePiece(Piece.WHITE, Position(3, 3), player1.user)

        assertIs<BoardWin>(board)
    }

    @Test
    fun `Winning Slash Move returns true`() {
        val board = BoardRun(
            boardSize = 10,
            winningLength = 5,
            overflowAllowed = false,
            player1 = player1,
            player2 = player2,
            turn = player1
        )
            .placePiece(Piece.WHITE, Position(5, 4), player1.user)
            .placePiece(Piece.WHITE, Position(6, 3), player2.user)
            .placePiece(Piece.WHITE, Position(3, 6), player1.user)
            .placePiece(Piece.WHITE, Position(2, 7), player2.user)
            .placePiece(Piece.WHITE, Position(4, 5), player1.user)

        assertIs<BoardWin>(board)
    }

    @Test
    fun `Overflow win when its not allowed does not return true`() {
        val board = BoardRun(
            boardSize = 10,
            winningLength = 5,
            overflowAllowed = false,
            player1 = player1,
            player2 = player2,
            turn = player1
        )
            .placePiece(Piece.WHITE, Position(3, 2), player1.user)
            .placePiece(Piece.WHITE, Position(4, 2), player2.user)
            .placePiece(Piece.WHITE, Position(0, 2), player1.user)
            .placePiece(Piece.WHITE, Position(1, 2), player2.user)
            .placePiece(Piece.WHITE, Position(5, 2), player1.user)
            .placePiece(Piece.WHITE, Position(2, 2), player2.user)

        assertIs<BoardRun>(board)
    }

    @Test
    fun testBoardRunPlacePiece() {
        val newBoard = boardRun.placePiece(piece, position, user1)
        assert(newBoard is BoardRun)
        assertEquals(Piece.WHITE, (newBoard as BoardRun).internalBoard[0][0])
    }

    @Test
    fun testBoardRunPlacePieceInvalidPosition() {
        assertThrows<InvalidPosition> {
            boardRun.placePiece(piece, Position(-1, -1), user1)
        }
    }

    @Test
    fun testBoardRunPlacePiecePositionAlreadyPlayed() {
        val newBoard = boardRun.placePiece(piece, position, user1)
        assertThrows<PositionAlreadyPlayed> {
            (newBoard as BoardRun).placePiece(piece, position, user2)
        }
    }

    @Test
    fun testBoardWinPlacePiece() {
        assertThrows<BoardIsBoardWin> {
            boardWin.placePiece(piece, position, user1)
        }
    }

    @Test
    fun testBoardDrawPlacePiece() {
        assertThrows<BoardIsBoardDraw> {
            boardDraw.placePiece(piece, position, user1)
        }
    }

    @Test
    fun testBoardRunUserToPlayer() {
        assertEquals(player1, boardRun.userToPlayer(user1))
    }

    @Test
    fun testBoardRunUserToPlayerInvalidUser() {
        assertThrows<UserNotInBoard> {
            boardRun.userToPlayer(User(3, "pass", "email@ggfg.", "password"))
        }
    }

    @Test
    fun testListSerializeToJsonString() {
        val list = listOf(listOf(Piece.WHITE, Piece.BLACK), listOf(Piece.BLACK, Piece.WHITE))
        val json = list.serializeToJsonString()
        assertEquals("[[\"WHITE\",\"BLACK\"],[\"BLACK\",\"WHITE\"]]", json)
    }

    @Test
    fun testStringParseJsonToBoard() {
        val json = "[[\"WHITE\",\"BLACK\"],[\"BLACK\",\"WHITE\"]]"
        val board = json.parseJsonToBoard()
        assertEquals(Piece.WHITE, board[0][0])
        assertEquals(Piece.BLACK, board[0][1])
        assertEquals(Piece.BLACK, board[1][0])
        assertEquals(Piece.WHITE, board[1][1])
    }
}
