package pt.isel.leic.daw.gomokuRoyale.domain

import pt.isel.leic.daw.gomokuRoyale.domain.user.User

data class Game internal constructor(
    val user1: User,
    val user2: User,
    private val settings: GameSettings,
    private val board: Board,
) {

    constructor(user1: User, user2: User, settings: GameSettings) :
            this(
                user1,
                user2,
                settings,
                BoardRun(
                    settings.boardSize,
                    settings.winningLength,
                    settings.overflowAllowed,
                    BlackPlayer(user1),
                    BlackPlayer(user1),
                    BlackPlayer(user2)
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
        require(user == user1 || user == user2)
        require(board is BoardRun)
        require(board.turn.user == user)

        val newBoard = board.placePiece(piece, position, user)
        val newUser1 = calculateUser(user1, newBoard, user2)
        val newUser2 = calculateUser(user1, newBoard, user1)

        return copy(board = newBoard, user1 = newUser1, user2 = newUser2)
    }
}
