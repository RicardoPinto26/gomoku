package pt.isel.leic.daw.gomokuRoyale.http.model.game

import pt.isel.leic.daw.gomokuRoyale.services.game.GameForfeitExternalInfo

data class GameForfeitOutputModel(val winner: String) {
    constructor(gfei: GameForfeitExternalInfo) : this(gfei.winner)
}
