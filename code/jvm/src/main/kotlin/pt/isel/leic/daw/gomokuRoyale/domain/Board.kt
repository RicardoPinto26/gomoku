package pt.isel.leic.daw.gomokuRoyale.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.*
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import kotlin.math.max
import kotlin.math.min

sealed interface Board {
    val internalBoard: List<List<Piece?>>

    fun placePiece(piece: Piece, position: Position, user: User): Board
}

/**
 * Board entity when game as finished with a win
 *
 * @property internalBoard List<<List<[Piece]?> representing the board
 * @property winner the player that won the game
 */
class BoardWin internal constructor(
    override val internalBoard: List<List<Piece?>>,
    val winner: Player
) : Board {

    /**
     * @throws BoardIsBoardWin exception if someone tries to place a piece
     */
    override fun placePiece(piece: Piece, position: Position, user: User): Board {
        throw BoardIsBoardWin("This game has already finished with a win.")
    }
}

/**
 * Board entity when game as finished with a draw
 *
 * @property internalBoard List<<List<[Piece]?> representing the board
 */
class BoardDraw internal constructor(
    override val internalBoard: List<List<Piece?>>
) : Board {

    /**
     * @throws BoardIsBoardDraw exception if someone tries to place a piece
     */
    override fun placePiece(piece: Piece, position: Position, user: User): Board {
        throw BoardIsBoardDraw("This game has already finished with a draw.")
    }
}

/**
 * Board entity when game is in progress
 *
 * @property winningLength amount of pieces in a line needed to win the game
 * @property overflowAllowed whether amount of pieces to win must be (exactly)/(at least) winningLength
 * @property player1 First [Player] to play
 * @property player2 Second [Player] to play
 * @property turn which [Player] is currently playing
 * @property internalBoard List<<List<[Piece]?> representing the board
 */
data class BoardRun internal constructor(
    private val winningLength: Int,
    private val overflowAllowed: Boolean,
    private val player1: Player,
    private val player2: Player,
    val turn: Player,
    override val internalBoard: List<List<Piece?>>
) : Board {

    constructor(
        boardSize: Int,
        winningLength: Int,
        overflowAllowed: Boolean,
        player1: Player,
        player2: Player,
        turn: Player
    ) : this(winningLength, overflowAllowed, player1, player2, turn, List(boardSize) { List(boardSize) { null } })

    /**
     * Places a piece in a given position
     * @param piece [Piece] to be placed
     * @param position [Position] to place piece
     * @param user [User] trying to place the piece
     *
     * @return new board with new piece in given position
     * @throws NotYourTurn exception if it's not the user's turn
     * @throws InvalidPosition exception if trying to play in invalid position
     * @throws PositionAlreadyPlayed if trying to play in occupied position
     */
    override fun placePiece(piece: Piece, position: Position, user: User): Board {
        if(user != turn.user) throw NotYourTurn("Not your turn, it's ${turn.user.username}'s turn")
        val boardSize = internalBoard.size
        if (position.row !in 0 until boardSize || position.column !in 0 until boardSize) {
            throw InvalidPosition("Position (${position.row},${position.column}) is invalid")
        }
        if (internalBoard[position.row][position.column] != null) {
            throw PositionAlreadyPlayed("Position $position already has a piece")
        }

        val newBoard = internalBoard.mapIndexed { row, list ->
            if (row == position.row) {
                list.mapIndexed { column, elem ->
                    if (column == position.column) {
                        piece
                    } else {
                        elem
                    }
                }
            } else {
                list
            }
        }

        return when {
            checkWin(newBoard, piece, position) -> BoardWin(newBoard, turn)
            newBoard.all { it.all { piece -> piece != null } } -> BoardDraw(newBoard)
            else -> this.copy(internalBoard = newBoard, turn = if (turn == player1) player2 else player1)
        }
    }

    /**
     * Checks if any vertical, horizontal or diagonal line from a given [Position] creates a win
     *
     * @param board list of pieces representing the board
     * @param piece [Piece] that counts towards a win
     * @param position to place piece
     *
     * @return true if there's a win false otherwise
     */
    private fun checkWin(board: List<List<Piece?>>, piece: Piece, position: Position): Boolean =
        checkVerticalWin(board, piece, position) ||
            checkHorizontalWin(board, piece, position) ||
            checkSlashAndBackslashWin(board, piece, position)

    /**
     * Checks if vertical line from a given [Position] creates a win
     *
     * @param board list of pieces representing the board
     * @param piece [Piece] that counts towards a win
     * @param position to place piece
     *
     * @return true if there's a win false otherwise
     */
    private fun checkVerticalWin(board: List<List<Piece?>>, piece: Piece, position: Position): Boolean {
        var winningPieces = 0
        val minVertical = max(0, (position.row - winningLength + 1))
        val maxVertical = min(board.size - 1, position.row + winningLength - 1)

        for (i in minVertical..maxVertical) {
            if (board[i][position.column] == piece) {
                winningPieces++
            } else {
                if (overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength) return true
            }
        }
        return overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength
    }

    /**
     * Checks if horizontal line from a given [Position] creates a win
     *
     * @param board list of pieces representing the board
     * @param piece [Piece] that counts towards a win
     * @param position to place piece
     *
     * @return true if there's a win false otherwise
     */
    private fun checkHorizontalWin(board: List<List<Piece?>>, piece: Piece, position: Position): Boolean {
        var winningPieces = 0
        val minHorizontal = max(0, (position.column - winningLength + 1))
        val maxHorizontal = min(board.size - 1, position.column + winningLength - 1)

        for (i in minHorizontal..maxHorizontal) {
            if (board[position.row][i] == piece) {
                winningPieces++
            } else {
                if (overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength) return true
            }
        }
        return overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength
    }

    /**
     * Checks if vertical line from a given [Position] creates a win
     *
     * @param board list of pieces representing the board
     * @param piece [Piece] that counts towards a win
     * @param position to place piece
     *
     * @return true if there's a win false otherwise
     */
    private fun checkSlashAndBackslashWin(board: List<List<Piece?>>, piece: Piece, position: Position): Boolean {
        var winningPieces = 0
        val min = max(0, max(position.column - winningLength + 1, position.row - winningLength + 1))
        val max = min(board.size - 1, min(position.column + winningLength - 1, position.row + winningLength - 1))
        for (direction in -1..1 step 2) {
            var column = if (direction == 1) min else max
            for (row in min..max) {
                if (board[row][column] == piece) {
                    winningPieces++
                } else {
                    if (overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength) return true
                }
                column += direction
            }
            if (overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength) return true
        }
        return false
    }

    /**
     * Converts [User] into [Player]
     *
     * @param user user to be converted
     *
     * @return Player
     */
    fun userToPlayer(user: User): Player =
        when (user) {
            player1.user -> player1
            player2.user -> player2
            else -> throw UserNotInBoard("User is not a player on this board")
        }
}

/**
 * Converts List<List<[Piece]?>> into a json string
 *
 * @return Json string
 */
fun List<List<Piece?>>.serializeToJsonString(): String {
    val objectMapper = ObjectMapper()
    return objectMapper.writeValueAsString(this)
}

/**
 * Converts json string into a List<List<[Piece]?>>
 *
 * @return List<List<Piece?>>
 */
fun String.parseJsonToBoard(): List<List<Piece?>> {
    val objectMapper = jacksonObjectMapper()
    return objectMapper.readValue(this)
}
