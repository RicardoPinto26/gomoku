package pt.isel.leic.daw.gomokuRoyale.http.controllers.lobbies.models

import pt.isel.leic.daw.gomokuRoyale.domain.Opening
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyExternalInfo

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
    val id: Int,
    val name: String,
    val gridSize: Int,
    val opening: String,
    val winningLength: Int,
    val pointsMargin: Int,
    val overflow: Boolean
) {
    constructor(lei: LobbyExternalInfo) : this(
        lei.id,
        "Lobby ${lei.id}",
        lei.gridSize,
        lei.opening,
        lei.winningLength,
        lei.pointsMargin,
        lei.overflow
    )
}
