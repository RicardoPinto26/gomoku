package pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models

import pt.isel.leic.daw.gomokuRoyale.domain.Position
import pt.isel.leic.daw.gomokuRoyale.services.game.GamePlayExternalInfo

/**
 * Game move output model
 *
 * @property username game name
 * @property board json string of the board after the move
 * @property lastMove [Position] of the last move
 *
 */
data class GamePlayOutputModel(val username: String, val board: String, val lastMove: Position) {
    constructor(gpei: GamePlayExternalInfo) : this(
        gpei.username,
        gpei.board,
        gpei.lastMove
    )
}
