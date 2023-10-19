package pt.isel.leic.daw.gomokuRoyale.http.model.lobby

import pt.isel.leic.daw.gomokuRoyale.services.lobby.LobbyExternalInfo

class LobbySeekOutputModel(
    val id: Int,
    val name: String,
    val gridSize: Int,
    val opening: String,
    val variant: String,
    val pointsMargin: Int
) {
    constructor(lei: LobbyExternalInfo) : this(
        lei.id,
        "Random Name",
        lei.gridSize,
        lei.opening,
        lei.variant,
        lei.pointsMargin
    )
}
