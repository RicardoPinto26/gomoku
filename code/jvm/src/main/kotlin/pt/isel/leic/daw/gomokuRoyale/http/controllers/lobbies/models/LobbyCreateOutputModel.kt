package pt.isel.leic.daw.gomokuRoyale.http.controllers.lobbies.models

import pt.isel.leic.daw.gomokuRoyale.domain.Opening
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyExternalInfo

/**
 * Lobby creation output information
 *
 * @property username name of the user that created the lobby
 * @property pointsMargin allowed ratings margin each [User]
 * @property gridSize size of the board where the game will take place
 * @property opening name of the [Opening]
 * @property winningLenght name of the variant to be played
 */
data class LobbyCreateOutputModel(
    val id: Int,
    val username: String,
    val pointsMargin: Int,
    val gridSize: Int,
    val opening: String,
    val winningLenght: Int,
    val overflow: Boolean
) {
    constructor(lei: LobbyExternalInfo) : this(
        lei.id,
        lei.user1.username,
        lei.pointsMargin,
        lei.gridSize,
        lei.opening,
        lei.winningLength,
        lei.overflow
    )
}
