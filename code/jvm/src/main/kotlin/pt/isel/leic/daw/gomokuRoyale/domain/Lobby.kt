package pt.isel.leic.daw.gomokuRoyale.domain

import java.util.Date

data class Lobby(
    private val game: Game? = null,
    private var playersIDS: Pair<Int, Int?>,
    private val pointsMargin: Int, // a 1400 point player creates a lobby with 400 point margin, he can match with a 1000 to 1800 point player
    private val startedAt: Date,
    private val fullLobby: Boolean = playersIDS.second != null
)
