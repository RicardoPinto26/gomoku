package pt.isel.leic.daw.gomokuRoyale.http.controllers.games.models

import pt.isel.leic.daw.gomokuRoyale.services.game.GameDTOExternalInfo
import pt.isel.leic.daw.gomokuRoyale.services.users.UserExternalInfo

/**
 * Game details output model
 *
 * @property gameId the unique identifier of the game created
 * @property user1 name of the user that created the lobby
 * @property user2 name of the user that joined the lobby
 * @property board json string of the board
 * @property state status of the game
 * @property turn the user that has the next turn
 */
/*class GameDetailsOutputModel(
    val gameId: Int,
    val user1: UserExternalInfo,
    val user2: UserExternalInfo,
    val board: String,
    val status: String,
    val turn: String?
) {
    constructor(gei: GameExternalInfo) : this(
        gei.id,
        gei.user1,
        gei.user2,
        gei.board,
        gei.status,
        gei.turn
    )
}*/
class GameDetailsOutputModel(
    val id: Int,
    val blackPlayer: UserExternalInfo,
    val whitePlayer: UserExternalInfo,
    val board: String,
    val turn: UserExternalInfo,
    val openingIndex: Int,
    val openingVariant: String?,
    val state: String,
    val winner: UserExternalInfo?
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
