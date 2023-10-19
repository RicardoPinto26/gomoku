package pt.isel.leic.daw.gomokuRoyale.domain

import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import java.util.Date

const val MAX_POINTS_MARGIN = Int.MAX_VALUE

data class Lobby(
    val name: String,
    val id: Int,
    private val game: Game? = null,
    val user1: User,
    val user2: User?,
    val pointsMargin: Int = MAX_POINTS_MARGIN, // a 1400 point player creates a lobby with 400 point margin, he can match with a 1000 to 1800 point player
    private val startedAt: Date,
    val settings: GameSettings
) {
    fun addUser(user: User): Lobby {
        require(user != user1)
        require(user2 == null)

        return copy(user2 = user, game = Game(name, user1, user, settings))
    }

    fun placePiece(piece: Piece, position: Position, user: User): Lobby {
        require(user == user1 || user == user2)
        require(game != null)

        val newGame = game.placePiece(piece, position, user)
        return copy(game = newGame)
    }

    fun compareUsers(userId: Int): Boolean {
        return userId == user1.id || userId == user2?.id
    }

    fun isLobbyFull(): Boolean {
        return user2 != null
    }

    fun isLobbyStarted(): Boolean {
        return game != null
    }

    fun isGameFinished(): Boolean {
        return game?.checkGameEnd() ?: false
    }
}
