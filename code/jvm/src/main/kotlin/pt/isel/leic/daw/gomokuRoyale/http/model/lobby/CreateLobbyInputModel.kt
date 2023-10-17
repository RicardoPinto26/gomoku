package pt.isel.leic.daw.gomokuRoyale.http.model.lobby

data class CreateLobbyInputModel(
    val gridSize: Int,
    val opening: String,
    val variant: String,
    val pointsMargin: Int
)
