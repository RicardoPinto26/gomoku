package pt.isel.leic.daw.gomokuRoyale.http.model.lobby

import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyExternalInfo

data class LobbyCreateOutputModel(
    val username: String,
    val pointsMargin: Int,
    val gridSize: Int,
    val opening: String,
    val variant: String
) {
    constructor(lei: LobbyExternalInfo) : this(lei.user1.username, lei.pointsMargin, lei.gridSize, lei.opening, lei.variant)
}