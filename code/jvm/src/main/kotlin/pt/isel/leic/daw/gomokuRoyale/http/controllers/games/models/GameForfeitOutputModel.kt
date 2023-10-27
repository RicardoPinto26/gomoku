package pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models

import pt.isel.leic.daw.gomokuRoyale.services.game.GameExternalInfo
import pt.isel.leic.daw.gomokuRoyale.services.users.UserExternalInfo

/**
 * Game forfeit output model
 *
 * @property winner name of the user that won the game
 */
data class GameForfeitOutputModel(val winner: UserExternalInfo) {
    constructor(gei: GameExternalInfo) : this(gei.winner!!)
}
