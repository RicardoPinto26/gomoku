package pt.isel.leic.daw.gomokuRoyale.services.lobby

import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.domain.user.User

interface LobbyService {
    fun createLobby(
        name: String,
        token: String,
        gridSize: Int,
        opening: String,
        variant: String,
        pointsMargin: Int
    ): LobbyCreationResult

    fun joinLobby(token: String, lobbyId: Int): LobbyJoinResult

    fun getLobbyByUserToken(token: String): Lobby?
    fun seekLobby(user: User, gridSize: Int, winningLength: Int, opening: String, pointsMargin: Int): LobbySeekResult
}
