package pt.isel.leic.daw.gomokuRoyale.http.model.game

import pt.isel.leic.daw.gomokuRoyale.domain.serializeToJsonString
import pt.isel.leic.daw.gomokuRoyale.services.game.GameCreationExternalInfo

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
