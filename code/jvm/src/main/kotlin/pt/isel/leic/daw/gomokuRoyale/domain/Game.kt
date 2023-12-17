package pt.isel.leic.daw.gomokuRoyale.domain

import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.BoardWrongType
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.InvalidPosition
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
            if (settings.opening == Opening.FREESTYLE || settings.opening == Opening.PRO || settings.opening == Opening.LONG_PRO) {
                BoardRun(
                    settings.boardSize,
                    settings.winningLength,
                    settings.overflowAllowed,
                    BlackPlayer(user1),
                    WhitePlayer(user2),
                    BlackPlayer(user1)
                )
            } else {
                BoardRun(
                    settings.boardSize,
                    settings.winningLength,
                    settings.overflowAllowed,
                    UnassignedPlayer(user1),
                    UnassignedPlayer(user2),
                    UnassignedPlayer(user1)
                )
            }
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

    fun chooseNextMove(move: Opening, user: User): Game {
        if (user != user1 && user != user2) throw UserNotInGame("User ${user.username} not in Game")

        val movesList = settings.opening.movesList
        require(currentOpeningIndex != -1 && movesList[currentOpeningIndex] == Opening.OpeningMove.CHOOSE_NEXT_MOVE)

        if (board !is BoardRun) throw BoardWrongType("Board type must be BoardRun")
        if (board.turn.user != user) throw UserWrongTurn("Not that user's turn")

        val opening = settings.opening

        for (variant in opening.variantList) {
            if (variant == move) {
                val newMovesList = variant.movesList
                var newOpeningIndex = if (newMovesList.isEmpty()) -1 else 0

                // swap color
                if (newMovesList[newOpeningIndex] == Opening.OpeningMove.SWAP_COLOR) {
                    newOpeningIndex = if (newOpeningIndex == newMovesList.lastIndex) {
                        -1
                    } else {
                        newOpeningIndex + 2
                    }
                    return copy(
                        settings = settings.copy(opening = variant),
                        currentOpeningIndex = newOpeningIndex,
                        board = (board.changePlayerColors())
                    )
                }

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

        val changeTurn = movesList[currentOpeningIndex - 2].name == "PLACE_BLACK" && piece == Piece.WHITE ||
            movesList[currentOpeningIndex - 2].name == "PLACE_WHITE" && piece == Piece.BLACK

        val otherUser = if (user == user1) user2 else user1
        val newBoard = board.chooseColor(piece, user, changeTurn, otherUser)
        val newOpeningIndex =
            if (currentOpeningIndex == movesList.lastIndex) {
                -1
            } else {
                currentOpeningIndex + 1
            }

        return copy(board = newBoard, currentOpeningIndex = newOpeningIndex)
    }

    fun placePiece(piece: Piece, position: Position, user: User): Game {
        val opening = settings.opening
        val movesList = opening.movesList

        require(currentOpeningIndex == -1 || movesList[currentOpeningIndex] == Opening.OpeningMove.placeColor(piece))
        validateUser(user, user1, user2, board)

        var newOpeningIndex =
            if (currentOpeningIndex == -1 || currentOpeningIndex == movesList.lastIndex) {
                -1
            } else {
                currentOpeningIndex + 1
            }

        if (opening == Opening.PRO || opening == Opening.LONG_PRO) {
            val centerRow = (board.internalBoard.size - 1) / 2
            val centerColumn = (board.internalBoard[0].size - 1) / 2
            checkOpeningRules(opening, position, centerRow, centerColumn)
        }

        val changeTurn =
            (newOpeningIndex != -1 && movesList[newOpeningIndex] == Opening.OpeningMove.CHANGE_PLAYER) ||
                (newOpeningIndex == -1)

        if (changeTurn) {
            newOpeningIndex =
                if (newOpeningIndex == movesList.lastIndex || newOpeningIndex == -1) {
                    -1
                } else if (movesList[newOpeningIndex + 1] == Opening.OpeningMove.CHANGE_PLAYER) {
                    newOpeningIndex + 2
                } else {
                    newOpeningIndex + 1
                }
        }

        val newBoard = board.placePiece(piece, position, user, changeTurn)

        val newUser1 = calculateUser(user1, newBoard, user2)
        val newUser2 = calculateUser(user2, newBoard, user1)

        return copy(board = newBoard, user1 = newUser1, user2 = newUser2, currentOpeningIndex = newOpeningIndex)
    }

    fun forfeitGame(): Game {
        if (board !is BoardRun) throw BoardWrongType("Board type must be BoardRun")
        val winner = if (board.turn.user == user1) user2 else user1
        val newBoard = BoardWin(board.internalBoard, board.userToPlayer(winner))
        val newUser1 = calculateUser(user1, newBoard, user2)
        val newUser2 = calculateUser(user2, newBoard, user1)
        return copy(board = newBoard, user1 = newUser1, user2 = newUser2)
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

    private fun checkOpeningRules(opening: Opening, position: Position, centerRow: Int, centerColumn: Int) {
        when (currentOpeningIndex) {
            0 -> {
                if (position.row != centerRow || position.column != centerColumn) {
                    throw InvalidPosition("Invalid position, must be on the center of the board")
                }
            }

            4 -> {
                if (opening == Opening.PRO && (
                    position.row !in centerRow - 1..centerRow + 1 ||
                        position.column !in centerColumn - 1..centerColumn + 1
                    )
                ) {
                    throw InvalidPosition("Invalid position, must be at most one intersection away from the first stone")
                }

                if (opening == Opening.LONG_PRO && (
                    position.row !in centerRow - 4..centerRow + 4 ||
                        position.column !in centerColumn - 4..centerColumn + 4
                    )
                ) {
                    throw InvalidPosition("Invalid position, must be within four intersections from the first stone")
                }
            }
        }
    }

    private fun validateUser(user: User, user1: User, user2: User, board: Board) {
        if (user != user1 && user != user2) throw UserNotInGame("User ${user.username} not in Game")
        if (board !is BoardRun) throw BoardWrongType("Board type must be BoardRun")
        if (board.turn.user != user) throw UserWrongTurn("Not that user's turn")
    }
}
