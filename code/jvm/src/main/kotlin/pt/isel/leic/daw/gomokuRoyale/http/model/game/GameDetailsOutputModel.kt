package pt.isel.leic.daw.gomokuRoyale.http.model.game

import pt.isel.leic.daw.gomokuRoyale.domain.serializeToJsonString
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.services.game.GameIdentificationExternalInfo

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
