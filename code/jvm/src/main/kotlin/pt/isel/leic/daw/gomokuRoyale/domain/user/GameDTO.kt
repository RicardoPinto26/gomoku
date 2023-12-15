package pt.isel.leic.daw.gomokuRoyale.domain.user

class GameDTO(
    val id: Int,
    val lobbyId: Int,
    val turn: Int,
    val blackPlayer: Int,
    val whitePlayer: Int,
    val winner: Int?,
    val openingIndex: Int,
    val openingVariant: String?,
    val state: String,
    val board: String
)
