package pt.isel.leic.daw.gomokuRoyale.http.controllers.lobbies.models

import pt.isel.leic.daw.gomokuRoyale.services.lobby.PublicLobbyExternalInfo
import pt.isel.leic.daw.gomokuRoyale.services.users.PublicUserExternalInfo

/**
 * Lobby join output information
 *
 * @property usernameCreator name of the user that created the lobby
 * @property usernameJoin name of the user that joined the lobby
 * @property lobbyId lobby unique identifier
 */
data class LobbyDetailsOutputModel(
    val id: Int, // lobbyId
    val name: String,
    val user1: PublicUserExternalInfo,
    val user2: PublicUserExternalInfo? = null,
    val gridSize: Int,
    val opening: String,
    val winningLength: Int,
    val pointsMargin: Int,
    val overflow: Boolean,
    val gameId: Int? = null
) {
    constructor(lei: PublicLobbyExternalInfo) : this(
        lei.id,
        lei.name,
        lei.user1,
        lei.user2,
        lei.gridSize,
        lei.opening,
        lei.winningLength,
        lei.pointsMargin,
        lei.overflow,
        lei.game?.id
    )
}
