package pt.isel.leic.daw.gomokuRoyale.services.lobby

import pt.isel.leic.daw.gomokuRoyale.domain.Lobby

interface LobbyService {
    fun createLobby(
        token: String,
        gridSize: Int,
        opening: String,
        variant: String,
        pointsMargin: Int
    ): LobbyCreationResult

    fun joinLobby(token: String, lobbyId: Int): LobbyJoinResult

    fun getLobbyByUserToken(token: String): Lobby?
}
