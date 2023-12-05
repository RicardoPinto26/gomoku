package pt.isel.leic.daw.gomokuRoyale.http.controllers.lobbies.models

import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyExternalInfo

/**
 * Lobby join output information
 *
 * @property usernameCreator name of the user that created the lobby
 * @property usernameJoin name of the user that joined the lobby
 * @property lobbyId lobby unique identifier
 */
data class LobbyDetailsOutputModel(
    val id: Int, // lobbyId
    val user1: User,
    val user2: User? = null,
    val gridSize: Int,
    val opening: String,
    val winningLength: Int,
    val pointsMargin: Int,
    val overflow: Boolean
) {
    constructor(lei: LobbyExternalInfo) : this(
        lei.id,
        lei.user1,
        lei.user2,
        lei.gridSize,
        lei.opening,
        lei.winningLength,
        lei.pointsMargin,
        lei.overflow
    )
}
