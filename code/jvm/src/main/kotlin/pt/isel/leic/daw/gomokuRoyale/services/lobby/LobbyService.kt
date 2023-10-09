package pt.isel.leic.daw.gomokuRoyale.services.lobby

import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.repository.jdbi.JdbiTransactionManager

class LobbyService(
    private val transactionManager: JdbiTransactionManager,
    private val lobbyDomain: Lobby
) : LobbyServiceInterface {

    override fun createLobby(
        username: String,
        gridSize: Int,
        opening: String,
        variant: String,
        pointsMargin: Int
    ): Int {
        TODO()
    }

    override fun joinLobby(userId: Int, lobbyId: Int): Int {
        TODO("Not yet implemented")
    }

    override fun getLobbyByUserId(userId: Int): Lobby? {
        TODO("Not yet implemented")
    }

}
