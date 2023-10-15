package pt.isel.leic.daw.gomokuRoyale.services.lobby

import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.repository.jdbi.JdbiTransactionManager
import pt.isel.leic.daw.gomokuRoyale.utils.failure
import pt.isel.leic.daw.gomokuRoyale.utils.success

class LobbyServiceImpl(
    private val transactionManager: JdbiTransactionManager, private val lobbyDomain: Lobby
) : LobbyServiceInterface {
    //TODO("use tokens instead of ids")
    override fun createLobby(
        username: String, gridSize: Int, opening: String, variant: String, pointsMargin: Int
    ): LobbyCreationResult {
        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository
            val userRepo = it.usersRepository
            val user = userRepo.getUserByUsername(username) ?: return@run failure(LobbyCreationError.UserNotFound)

            try {
                val id = lobbyRepo.createLobby(user.id, gridSize, opening, variant, pointsMargin)
                return@run success(id)
            } catch (e: Exception) {
                return@run failure(LobbyCreationError.UnknownError)
            }
        }
    }

    override fun joinLobby(username: String, lobbyId: Int): LobbyJoinResult {
        return transactionManager.run {
            val userRepo = it.usersRepository
            val user = userRepo.getUserByUsername(username) ?: return@run failure(LobbyJoinError.UserNotFound)
            val lobbyRepo = it.lobbyRepository
            val lobby = lobbyRepo.getLobbyById(lobbyId) ?: return@run failure(LobbyJoinError.LobbyNotFound)
            if (lobby.compareUsers(user.id)) return@run failure(LobbyJoinError.UserAlreadyInLobby)
            if (lobby.isLobbyFull()) return@run failure(LobbyJoinError.LobbyFull)

            try {
                val id = lobbyRepo.joinLobby(user.id, lobbyId)
                return@run success(id)
            } catch (e: Exception) {
                return@run failure(LobbyJoinError.UnknownError)
            }

        }
    }

    override fun getLobbyByUserId(userId: Int): Lobby? {
        TODO("Not yet implemented")
    }

}
