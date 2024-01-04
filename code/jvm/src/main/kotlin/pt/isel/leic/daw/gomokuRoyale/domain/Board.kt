package pt.isel.leic.daw.gomokuRoyale.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.BoardIsBoardDraw
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.BoardIsBoardWin
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.InvalidPosition
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.NotYourTurn
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.PositionAlreadyPlayed
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserNotInBoard
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import kotlin.math.max
import kotlin.math.min

sealed interface Board {
    val internalBoard: List<List<Piece?>>

    fun chooseColor(color: Piece, user: User, changeTurn: Boolean = true, otherUser: User): Board
    fun placePiece(piece: Piece, position: Position, user: User, changeTurn: Boolean = true): Board
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
    override fun chooseColor(color: Piece, user: User, changeTurn: Boolean, otherUser: User): Board {
        throw BoardIsBoardWin("This game has already finished with a win.")
    }

    /**
     * @throws BoardIsBoardWin exception if someone tries to place a piece
     */
    override fun placePiece(piece: Piece, position: Position, user: User, changeTurn: Boolean): Board {
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

    override fun chooseColor(color: Piece, user: User, changeTurn: Boolean, otherUser: User): Board {
        throw BoardIsBoardDraw("This game has already finished with a draw.")
    }

    /**
     * @throws BoardIsBoardDraw exception if someone tries to place a piece
     */
    override fun placePiece(piece: Piece, position: Position, user: User, changeTurn: Boolean): Board {
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
class BoardRun internal constructor(
    private val winningLength: Int,
    private val overflowAllowed: Boolean,
    val player1: Player,
    val player2: Player,
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

    override fun chooseColor(color: Piece, user: User, changeTurn: Boolean, otherUser: User): Board {
        val (newPlayer, otherNewPlayer) = when (color) {
            Piece.BLACK -> BlackPlayer(user) to WhitePlayer(otherUser)
            Piece.WHITE -> WhitePlayer(user) to BlackPlayer(otherUser)
        }

        return BoardRun(
            winningLength,
            overflowAllowed,
            if (color == Piece.BLACK) newPlayer else otherNewPlayer,
            if (color == Piece.WHITE) newPlayer else otherNewPlayer,
            if (changeTurn) otherNewPlayer else newPlayer,
            internalBoard
        )
    }

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
    override fun placePiece(piece: Piece, position: Position, user: User, changeTurn: Boolean): Board {
        if (user != turn.user) throw NotYourTurn("Not your turn, it's ${turn.user.username}'s turn")
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
            else -> BoardRun(
                winningLength,
                overflowAllowed,
                player1,
                player2,
                if (turn == player1) {
                    when (changeTurn) {
                        true -> player2
                        false -> player1
                    }
                } else {
                    when (changeTurn) {
                        true -> player1
                        false -> player2
                    }
                },
                newBoard
            )
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

    private fun checkSlashAndBackslashWin(board: List<List<Piece?>>, piece: Piece, position: Position): Boolean {
        return checkSlashWin(board, piece, position) || checkBackslashWin(board, piece, position)
    }

    private fun checkSlashWin(board: List<List<Piece?>>, piece: Piece, position: Position): Boolean {
        val a = min(position.row, position.column)
        val b = if (position.row > position.column) board.size - 1 - position.row else board.size - 1 - position.column

        val minRow = max(0, position.row - a)
        val minColumn = max(0, position.column - a)
        val maxRow = min(board.size - 1, position.row + b)
        val maxColumn = min(board.size - 1, position.column + b)

        println("checking slash from ($minRow, $minColumn) to ($maxRow, $maxColumn)")
        var winningPieces = 0
        var row = minRow
        var column = maxColumn
        while (row <= maxRow && column <= maxColumn) {
            if (board[row][column] == piece) {
                winningPieces++
            } else {
                if (overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength) return true
                winningPieces = 0
            }

            row++
            column--
        }
        return overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength
    }

    private fun checkBackslashWin(board: List<List<Piece?>>, piece: Piece, position: Position): Boolean {
        val a = min(position.row, position.column)
        val b = if (position.row > position.column) board.size - 1 - position.row else board.size - 1 - position.column

        val minRow = max(0, position.row - a)
        val minColumn = max(0, position.column - a)
        val maxRow = min(board.size - 1, position.row + b)
        val maxColumn = min(board.size - 1, position.column + b)

        println("checking backslash from ($minRow, $minColumn) to ($maxRow, $maxColumn)")
        var winningPieces = 0
        var row = minRow
        var column = minColumn
        while (row <= maxRow && column <= maxColumn) {
            if (board[row][column] == piece) {
                winningPieces++
            } else {
                if (overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength) return true
                winningPieces = 0
            }

            row++
            column++
        }
        return overflowAllowed && winningPieces >= winningLength || winningPieces == winningLength
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

    fun changePlayerColors(): BoardRun {
        val newPlayer1 = if (player1 is BlackPlayer) BlackPlayer(player2.user) else player1
        val newPlayer2 = if (player2 is WhitePlayer) WhitePlayer(player1.user) else player2
        return BoardRun(
            winningLength,
            overflowAllowed,
            newPlayer1,
            newPlayer2,
            if (newPlayer1 is WhitePlayer) newPlayer1 else newPlayer2,
            internalBoard
        )
    }

    fun changeTurn(): BoardRun {
        val newTurn = if (turn == player1) player2 else player1
        return BoardRun(
            winningLength,
            overflowAllowed,
            player1,
            player2,
            newTurn,
            internalBoard
        )
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
