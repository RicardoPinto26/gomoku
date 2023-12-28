package pt.isel.leic.daw.gomokuRoyale.http.controllers.lobbies.models

import pt.isel.leic.daw.gomokuRoyale.domain.Opening
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.services.lobby.PublicLobbyExternalInfo
import pt.isel.leic.daw.gomokuRoyale.services.users.PublicUserExternalInfo

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
    val user1: PublicUserExternalInfo, // user1
    val user2: PublicUserExternalInfo? = null, // user2
    val lobbyId: Int,
    val gameId: Int? = null,
    val gridSize: Int,
    val opening: String,
    val winningLength: Int,
    val pointsMargin: Int,
    val overflow: Boolean
) {
    constructor(plei: PublicLobbyExternalInfo) : this(
        plei.user1,
        plei.user2,
        plei.id,
        plei.game?.id,
        plei.gridSize,
        plei.opening,
        plei.winningLength,
        plei.pointsMargin,
        plei.overflow
    )
}
