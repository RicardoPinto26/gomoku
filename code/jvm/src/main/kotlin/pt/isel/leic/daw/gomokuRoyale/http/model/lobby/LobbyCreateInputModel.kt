package pt.isel.leic.daw.gomokuRoyale.http.model.lobby

data class LobbyCreateInputModel(
    val name: String,
    val gridSize: Int,
    val opening: String,
    val variant: String,
    val pointsMargin: Int
)
