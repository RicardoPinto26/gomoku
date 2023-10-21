package pt.isel.leic.daw.gomokuRoyale.http.controllers.lobbies.models

import pt.isel.leic.daw.gomokuRoyale.domain.Opening
import pt.isel.leic.daw.gomokuRoyale.domain.user.User

/**
 * Lobby creation input information
 *
 * @property name name of the lobby
 * @property gridSize size of the board where the game will take place
 * @property opening name of the [Opening]
 * @property variant name of the variant to be played
 * @property pointsMargin allowed ratings margin each [User]
 *
 */
data class LobbyCreateInputModel(
    val name: String,
    val gridSize: Int,
    val opening: String,
    val variant: String,
    val pointsMargin: Int
)
