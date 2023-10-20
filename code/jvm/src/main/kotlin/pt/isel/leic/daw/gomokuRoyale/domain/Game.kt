package pt.isel.leic.daw.gomokuRoyale.domain

import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.BoardWrongType
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserNotInGame
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserWrongTurn
import pt.isel.leic.daw.gomokuRoyale.domain.user.User

data class Game internal constructor(
    val name: String,
    val user1: User,
    val user2: User,
    private val settings: GameSettings,
    val board: Board
) {

    constructor(name: String, user1: User, user2: User, settings: GameSettings) :
        this(
            name,
            user1,
            user2,
            settings,
            BoardRun(
                settings.boardSize,
                settings.winningLength,
                settings.overflowAllowed,
                BlackPlayer(user1),
                BlackPlayer(user1),
                WhitePlayer(user2)
            )
        )

    private fun calculateUser(user: User, board: Board, otherUser: User): User =
        when (board) {
            is BoardRun -> user
            is BoardWin -> {
                val outcome = if (user == board.winner.user) 1.0 else 0.0
                user.copy(rating = user.calculateNewRating(outcome, otherUser.rating))
            }

            is BoardDraw -> user.copy(rating = user.calculateNewRating(0.5, otherUser.rating))
        }

    fun placePiece(piece: Piece, position: Position, user: User): Game {
        if (user != user1 && user != user2) throw UserNotInGame("User ${user.username} not in Game")
        if (board !is BoardRun) throw BoardWrongType("Board type must be BoardRun")
        if (board.turn.user != user) throw UserWrongTurn("Not that user's turn")

        val newBoard = board.placePiece(piece, position, user)

        val newUser1 = calculateUser(user1, newBoard, user2)
        val newUser2 = calculateUser(user1, newBoard, user1)

        return copy(board = newBoard, user1 = newUser1, user2 = newUser2)
    }

    fun forfeitGame(): Game {
        if (board !is BoardRun) throw BoardWrongType("Board type must be BoardRun")
        val winner = if (board.turn.user == user1) user2 else user1
        val newBoard = BoardWin(board.internalBoard, board.userToPlayer(winner))
        return copy(board = newBoard)
    }

    fun checkGameEnd(): Boolean {
        return board is BoardWin || board is BoardDraw
    }

    fun checkUserInGame(userId: Int): User? {
        return when (userId) {
            user1.id -> user1
            user2.id -> user2
            else -> null
        }
    }
}
