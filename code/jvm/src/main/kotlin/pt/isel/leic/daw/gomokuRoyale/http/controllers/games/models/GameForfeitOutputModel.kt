package pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models

import pt.isel.leic.daw.gomokuRoyale.services.game.GameForfeitExternalInfo

/**
 * Game forfeit output model
 *
 * @property winner name of the user that won the game
 */
data class GameForfeitOutputModel(val winner: String) {
    constructor(gfei: GameForfeitExternalInfo) : this(gfei.winner)
}
