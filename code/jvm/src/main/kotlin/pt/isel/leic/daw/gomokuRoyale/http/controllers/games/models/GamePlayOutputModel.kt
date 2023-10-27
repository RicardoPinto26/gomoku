package pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models

import pt.isel.leic.daw.gomokuRoyale.domain.Position
import pt.isel.leic.daw.gomokuRoyale.services.game.GameExternalInfo

/**
 * Game move output model
 *
 * @property username game name
 * @property board json string of the board after the move
 * @property lastMove [Position] of the last move
 *
 */
data class GamePlayOutputModel(
    val username: String,
    val board: String
) {
    constructor(gei: GameExternalInfo) : this(
        gei.user1.username,
        gei.board
    )
}
