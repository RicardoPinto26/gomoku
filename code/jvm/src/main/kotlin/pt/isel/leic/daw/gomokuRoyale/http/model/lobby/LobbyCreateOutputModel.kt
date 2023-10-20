package pt.isel.leic.daw.gomokuRoyale.http.model.lobby

import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyExternalInfo
import pt.isel.leic.daw.gomokuRoyale.domain.Opening
import pt.isel.leic.daw.gomokuRoyale.domain.user.User

/**
 * Lobby creation output information
 *
 * @property username name of the user that created the lobby
 * @property pointsMargin allowed ratings margin each [User]
 * @property gridSize size of the board where the game will take place
 * @property opening name of the [Opening]
 * @property variant name of the variant to be played
 */
data class LobbyCreateOutputModel(
    val username: String,
    val pointsMargin: Int,
    val gridSize: Int,
    val opening: String,
    val variant: String
) {
    constructor(lei: LobbyExternalInfo) : this(
        lei.user1.username,
        lei.pointsMargin,
        lei.gridSize,
        lei.opening,
        lei.variant
    )
}
