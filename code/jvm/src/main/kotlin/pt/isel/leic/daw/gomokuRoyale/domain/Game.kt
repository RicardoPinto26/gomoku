package pt.isel.leic.daw.gomokuRoyale.domain

import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.BoardWrongType
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserNotInGame
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserWrongTurn
import pt.isel.leic.daw.gomokuRoyale.domain.user.User

data class Game internal constructor(
    val name: String,
    val user1: User,
    val user2: User,
    val settings: GameSettings,
    val currentOpeningIndex: Int,
    val board: Board
) {

    constructor(name: String, user1: User, user2: User, settings: GameSettings) :
        this(
            name,
            user1,
            user2,
            settings,
            if (settings.opening.movesList.isEmpty()) -1 else 0,
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

    fun chooseNextMove(move: Opening.OpeningMove, user: User): Game {
        if (user != user1 && user != user2) throw UserNotInGame("User ${user.username} not in Game")

        val movesList = settings.opening.movesList
        require(currentOpeningIndex != -1 && movesList[currentOpeningIndex] == Opening.OpeningMove.CHOOSE_NEXT_MOVE)

        if (board !is BoardRun) throw BoardWrongType("Board type must be BoardRun")
        if (board.turn.user != user) throw UserWrongTurn("Not that user's turn")

        val opening = settings.opening

        for (variant in opening.variantList) {
            val newMovesList = variant.movesList
            if (newMovesList.firstOrNull() == move) {
                if (newMovesList.size == 1) {
                    return copy(
                        settings = settings.copy(opening = variant),
                        currentOpeningIndex = -1,
                        board = board
                    )
                }

                var newOpeningIndex = if (newMovesList.isEmpty()) -1 else 0

                val changeTurn =
                    newOpeningIndex != -1 && newMovesList[newOpeningIndex] == Opening.OpeningMove.CHANGE_PLAYER

                if (changeTurn) {
                    newOpeningIndex =
                        if (newOpeningIndex == newMovesList.lastIndex) {
                            -1
                        } else {
                            1
                        }
                }

                return copy(
                    settings = settings.copy(opening = variant),
                    currentOpeningIndex = newOpeningIndex,
                    board = if (changeTurn) board.changeTurn() else board
                )
            }
        }

        throw Exception("Not a valid move")
    }

    fun chooseColor(piece: Piece, user: User): Game {
        if (user != user1 && user != user2) throw UserNotInGame("User ${user.username} not in Game")

        val movesList = settings.opening.movesList
        require(currentOpeningIndex != -1 && movesList[currentOpeningIndex] == Opening.OpeningMove.CHOOSE_COLOR)

        if (board !is BoardRun) throw BoardWrongType("Board type must be BoardRun")
        if (board.turn.user != user) throw UserWrongTurn("Not that user's turn")

        val newBoard = board.chooseColor(piece, user)

        return copy(board = newBoard)
    }

    fun placePiece(piece: Piece, position: Position, user: User): Game {
        val movesList = settings.opening.movesList

        require(currentOpeningIndex == -1 || movesList[currentOpeningIndex] == Opening.OpeningMove.placeColor(piece))

        var newOpeningIndex =
            if (currentOpeningIndex == -1 || currentOpeningIndex == movesList.lastIndex) {
                -1
            } else {
                currentOpeningIndex + 1
            }

        if (user != user1 && user != user2) throw UserNotInGame("User ${user.username} not in Game")
        if (board !is BoardRun) throw BoardWrongType("Board type must be BoardRun")
        if (board.turn.user != user) throw UserWrongTurn("Not that user's turn")

        val changeTurn =
            newOpeningIndex != -1 && movesList[newOpeningIndex] == Opening.OpeningMove.CHANGE_PLAYER

        if (changeTurn) newOpeningIndex = if (newOpeningIndex == movesList.lastIndex) -1 else newOpeningIndex + 1

        val newBoard = board.placePiece(piece, position, user, changeTurn)

        val newUser1 = calculateUser(user1, newBoard, user2)
        val newUser2 = calculateUser(user1, newBoard, user1)

        return copy(board = newBoard, user1 = newUser1, user2 = newUser2, currentOpeningIndex = newOpeningIndex)
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
