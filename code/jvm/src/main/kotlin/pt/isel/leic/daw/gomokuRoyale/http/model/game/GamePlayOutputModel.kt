package pt.isel.leic.daw.gomokuRoyale.http.model.game

import pt.isel.leic.daw.gomokuRoyale.domain.Position
import pt.isel.leic.daw.gomokuRoyale.services.game.GamePlayExternalInfo

data class GamePlayOutputModel(val username: String, val board: String, val lastMove: Position) {
    constructor(gpei: GamePlayExternalInfo) : this(
        gpei.username,
        gpei.board,
        gpei.lastMove
    )
}
