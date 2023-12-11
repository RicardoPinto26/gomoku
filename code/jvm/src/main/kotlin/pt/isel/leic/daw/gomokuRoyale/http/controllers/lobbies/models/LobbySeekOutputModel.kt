package pt.isel.leic.daw.gomokuRoyale.http.controllers.lobbies.models

import pt.isel.leic.daw.gomokuRoyale.domain.Opening
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.services.lobby.PublicLobbyExternalInfo

/**
 * Lobby seek output information
 *
 * @property id lobby unique identifier
 * @property name name of the lobby
 * @property gridSize size of the board in the lobby
 * @property opening name of the [Opening]
 * @property winningLength name of the variant to be played
 * @property pointsMargin allowed ratings margin each [User]
 */
class LobbySeekOutputModel(
    val usernameCreator: String, // user1
    val usernameJoin: String? = null, // user2
    val lobbyId: Int,
    val gameId: Int? = null,
    val gridSize: Int,
    val opening: String,
    val winningLength: Int,
    val pointsMargin: Int,
    val overflow: Boolean
) {
    constructor(plei: PublicLobbyExternalInfo) : this(
        plei.user1.username,
        plei.user2?.username,
        plei.id,
        plei.game?.id,
        plei.gridSize,
        plei.opening,
        plei.winningLength,
        plei.pointsMargin,
        plei.overflow
    )
}
