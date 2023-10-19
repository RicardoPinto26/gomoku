package pt.isel.leic.daw.gomokuRoyale.http.model.lobby

data class LobbySeekInputModel(
    val gridSize: Int,
    val winningLength: Int,
    val opening: String,
    val pointsMargin: Int
)
