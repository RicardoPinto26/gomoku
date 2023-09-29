package pt.isel.leic.daw.gomokuRoyale.repository.lobby

import org.apache.catalina.User
import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.domain.Opening

interface JdbiLobbyInterface {
    fun createLobby(userId: Int, gridSize: Int, opening: Opening, variant: String, pointsMargin: Int): Int

    fun getUserLobbys(userId: Int): List<User>

    fun getLobbyByOpening(opening: String): Lobby?

    fun getLobbyByVariant(variant: String): Lobby?
}
