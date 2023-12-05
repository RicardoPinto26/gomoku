package pt.isel.leic.daw.gomokuRoyale.http.controllers.lobbies.models

import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyJoinExternalInfo

/**
 * Lobby join output information
 *
 * @property usernameCreator name of the user that created the lobby
 * @property usernameJoin name of the user that joined the lobby
 * @property lobbyId lobby unique identifier
 */
data class LobbyJoinOutputModel(
    val usernameCreator: String,
    val usernameJoin: String,
    val lobbyId: Int,
    val gameId: Int
) {
    constructor(uei: LobbyJoinExternalInfo) : this(uei.usernameCreator, uei.usernameJoin, uei.lobbyId, uei.gameId)
}
