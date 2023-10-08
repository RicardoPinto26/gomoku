package pt.isel.leic.daw.gomokuRoyale.services.lobby

import pt.isel.leic.daw.gomokuRoyale.domain.Lobby

interface LobbyServiceInterface {
    fun createLobby(username: String, gridSize: Int, opening: String, variant: String, pointsMargin: Int): Int

    fun joinLobby(userId: Int, lobbyId: Int): Int

    fun getLobbyByUserId(userId: Int): Lobby?
}