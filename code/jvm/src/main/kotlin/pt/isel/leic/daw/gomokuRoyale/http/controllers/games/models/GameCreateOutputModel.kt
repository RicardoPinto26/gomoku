package pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models

import pt.isel.leic.daw.gomokuRoyale.services.game.GameExternalInfo
import pt.isel.leic.daw.gomokuRoyale.services.users.UserExternalInfo

/**
 * Game creation output model
 *
 * @property user1 name of the user that created the lobby
 * @property user2 name of the user that joined the lobby
 * @property board json string of the board
 * @property lobbyId unique identifier of the lobby hosting the game
 */
data class GameCreateOutputModel(
    val id: Int,
    val user1: UserExternalInfo,
    val user2: UserExternalInfo,
    val board: String,
    val lobbyId: Int
) {
    constructor(gei: GameExternalInfo) : this(
        gei.id,
        gei.user1,
        gei.user2,
        gei.board,
        gei.lobbyID
    )
}
