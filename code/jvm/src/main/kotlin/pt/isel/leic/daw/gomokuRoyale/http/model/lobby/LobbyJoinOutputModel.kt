package pt.isel.leic.daw.gomokuRoyale.http.model.lobby

import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyJoinExternalInfo

data class LobbyJoinOutputModel(
    val usernameCreator: String,
    val usernameJoin: String,
    val lobbyId: Int
) {
    constructor(uei: LobbyJoinExternalInfo) : this(uei.usernameCreator, uei.usernameJoin, uei.lobbyId)
}
