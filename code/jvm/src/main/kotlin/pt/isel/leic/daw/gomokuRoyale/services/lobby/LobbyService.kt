package pt.isel.leic.daw.gomokuRoyale.services.lobby

import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.domain.user.User

interface LobbyService {
    fun createLobby(
        user: User,
        name: String,
        gridSize: Int,
        opening: String,
        winningLength: Int,
        pointsMargin: Int,
        overflow: Boolean
    ): LobbyCreationResult

    fun joinLobby(user: User, lobbyId: Int): LobbyJoinResult

    fun getLobbyByUserToken(token: String): Lobby?

    fun seekLobby(
        user: User,
        gridSize: Int,
        winningLength: Int,
        opening: String,
        overflow: Boolean,
        pointsMargin: Int
    ): LobbySeekResult

    fun getAvailableLobbies(user: User): LobbiesAvailableResult

    fun getLobbyDetails(user: User, lobbyId: Int): LobbyDetailsResult
}
