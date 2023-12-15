package pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models

import pt.isel.leic.daw.gomokuRoyale.services.game.GameDTOExternalInfo
import pt.isel.leic.daw.gomokuRoyale.services.game.GameExternalInfo
import pt.isel.leic.daw.gomokuRoyale.services.users.UserExternalInfo

/**
 * Game move output model
 *
 * @property username game name
 * @property board json string of the board after the move
 *
 */
data class GamePlayOutputModel(
    val id: Int,
    val blackPlayer: UserExternalInfo,
    val whitePlayer: UserExternalInfo,
    val board: String,
    val turn: UserExternalInfo,
    val openingIndex: Int,
    val openingVariant: String?,
    val status: String,
    val winner: UserExternalInfo? = null
) {
    constructor(gei: GameDTOExternalInfo) : this(
        gei.id,
        gei.blackPlayer,
        gei.whitePlayer,
        gei.board,
        gei.turn,
        gei.openingIndex,
        gei.openingVariant,
        gei.status,
        gei.winner
    )
}
