package pt.isel.leic.daw.gomokuRoyale.repository.lobby

import pt.isel.leic.daw.gomokuRoyale.domain.Lobby

interface LobbyRepository {
    fun createLobby(name: String, userId: Int, gridSize: Int, opening: String, variant: String, pointsMargin: Int): Int
    fun joinLobby(userId: Int, lobbyId: Int): Int

    fun getLobbyById(lobbyId: Int): Lobby?

    fun getUserLobbys(userId: Int): List<Lobby>

    fun getLobbyByOpening(opening: String): Lobby?

    fun getLobbyByVariant(variant: String): Lobby?

    fun seekLobbyID(
            otherRating: Int,
            gridSize: Int,
            winningLength: Int,
            opening: String,
            minRating: Int,
            maxRating: Int
    ): Int?
}
