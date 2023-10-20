package pt.isel.leic.daw.gomokuRoyale.http.model.lobby

import pt.isel.leic.daw.gomokuRoyale.domain.Opening
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
/**
 * Lobby seek input information
 *
 * @property gridSize size of the board where the game will take place
 * @property winningLength amount of pieces in a line needed to win
 * @property opening name of the [Opening]
 * @property pointsMargin allowed ratings margin each [User]
 *
 */
data class LobbySeekInputModel(
    val gridSize: Int,
    val winningLength: Int,
    val opening: String,
    val pointsMargin: Int
)
