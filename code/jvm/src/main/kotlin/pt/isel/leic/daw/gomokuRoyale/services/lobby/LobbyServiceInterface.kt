package pt.isel.leic.daw.gomokuRoyale.services.lobby

import pt.isel.leic.daw.gomokuRoyale.domain.Lobby

interface LobbyServiceInterface {
    fun createLobby(username: String, gridSize: Int, opening: String, variant: String, pointsMargin: Int): LobbyCreationResult

    fun joinLobby(username: String, lobbyId: Int): LobbyJoinResult

    fun getLobbyByUserId(userId: Int): Lobby?
}