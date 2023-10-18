package pt.isel.leic.daw.gomokuRoyale.repository.lobby

import org.apache.catalina.User
import pt.isel.leic.daw.gomokuRoyale.domain.Lobby

interface LobbyRepositoryInterface {
    fun createLobby(name: String, userId: Int, gridSize: Int, opening: String, variant: String, pointsMargin: Int): Int

    fun joinLobby(userId: Int, lobbyId: Int): Int

    fun getLobbyById(lobbyId: Int): Lobby?

    fun getUserLobbys(userId: Int): List<User>

    fun getLobbyByOpening(opening: String): Lobby?

    fun getLobbyByVariant(variant: String): Lobby?
}
