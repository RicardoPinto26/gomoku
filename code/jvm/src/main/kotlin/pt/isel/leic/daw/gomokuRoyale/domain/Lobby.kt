package pt.isel.leic.daw.gomokuRoyale.domain

import java.util.Date
import pt.isel.leic.daw.gomokuRoyale.domain.user.User

data class Lobby(
    val id: Int,
    private val game: Game? = null,
    val user1: User,
    private val user2: User?,
    private val pointsMargin: Int, // a 1400 point player creates a lobby with 400 point margin, he can match with a 1000 to 1800 point player
    private val startedAt: Date,
    private val settings: GameSettings
) {
    fun addUser(user: User): Lobby {
        require(user != user1)
        require(user2 == null)

        return copy(user2 = user, game = Game(user1, user, settings))
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
}
