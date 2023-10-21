package pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models

import pt.isel.leic.daw.gomokuRoyale.domain.serializeToJsonString
import pt.isel.leic.daw.gomokuRoyale.services.game.GameCreationExternalInfo

/**
 * Game creation output model
 *
 * @property name game name
 * @property user1 name of the user that created the lobby
 * @property user2 name of the user that joined the lobby
 * @property board json string of the board
 * @property lobbyId unique identifier of the lobby hosting the game
 */
data class GameCreateOutputModel(
    val name: String,
    val user1: String,
    val user2: String,
    val board: String,
    val lobbyId: Int
) {
    constructor(lei: GameCreationExternalInfo) : this(
        lei.name,
        lei.user1,
        lei.user2,
        lei.board.internalBoard.serializeToJsonString(),
        lei.lobbyId
    )
}
