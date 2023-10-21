package pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models

import pt.isel.leic.daw.gomokuRoyale.domain.serializeToJsonString
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.services.game.GameIdentificationExternalInfo

/**
 * Game details output model
 *
 * @property gameId the unique identifier of the game created
 * @property user1 name of the user that created the lobby
 * @property user2 name of the user that joined the lobby
 * @property board json string of the board
 */
class GameDetailsOutputModel(
    val gameId: Int,
    val user1: User,
    val user2: User,
    val board: String
) {
    constructor(giei: GameIdentificationExternalInfo) : this(
        giei.id,
        giei.user1,
        giei.user2,
        giei.board.internalBoard.serializeToJsonString()
    )
}
