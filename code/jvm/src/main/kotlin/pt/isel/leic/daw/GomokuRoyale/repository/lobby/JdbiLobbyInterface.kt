package pt.isel.leic.daw.GomokuRoyale.repository.lobby

import org.apache.catalina.User
import pt.isel.leic.daw.GomokuRoyale.domain.Lobby
import pt.isel.leic.daw.GomokuRoyale.domain.Opening

interface JdbiLobbyInterface {
    fun createLobby(userId: Int, gridSize: Int, opening: Opening, variant: String, pointsMargin: Int): Int

    fun getUserLobbys(userId: Int): List<User>

    fun getLobbyByOpening(opening: String): Lobby?

    fun getLobbyByVariant(variant: String): Lobby?
}