package pt.isel.leic.daw.gomokuRoyale.repository.lobby

import pt.isel.leic.daw.gomokuRoyale.domain.Lobby

interface LobbyRepository {
    fun createLobby(
        name: String,
        userId: Int,
        gridSize: Int,
        opening: String,
        winningLenght: Int,
        pointsMargin: Int,
        overflow: Boolean
    ): Int

    fun joinLobby(userId: Int, lobbyId: Int): Int

    fun getLobbyById(lobbyId: Int): Lobby?

    fun getUserLobbys(userId: Int): List<Lobby>

    fun getLobbyByOpening(opening: String): Lobby?

    fun getLobbyByGameId(gameId: Int): Lobby?

    fun seekLobbyID(
        otherRating: Int,
        gridSize: Int,
        winningLength: Int,
        opening: String,
        overflow: Boolean,
        minRating: Int,
        maxRating: Int
    ): Int?

    fun getAvailableLobbies(): List<Lobby>
}
