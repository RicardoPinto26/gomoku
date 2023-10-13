package pt.isel.leic.daw.gomokuRoyale.services.lobby

import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.repository.jdbi.JdbiTransactionManager
import pt.isel.leic.daw.gomokuRoyale.utils.failure
import pt.isel.leic.daw.gomokuRoyale.utils.success

class LobbyServiceImpl(
    private val transactionManager: JdbiTransactionManager, private val lobbyDomain: Lobby
) : LobbyServiceInterface {

    override fun createLobby(
        username: String, gridSize: Int, opening: String, variant: String, pointsMargin: Int
    ): LobbyCreationResult {
        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository
            val userRepo = it.usersRepository
            val user = userRepo.getUserByUsername(username)
            if (user != null) {
                try {
                    val id = lobbyRepo.createLobby(user.id, gridSize, opening, variant, pointsMargin)
                    return@run success(id)
                } catch (e: Exception) {
                    return@run failure(LobbyCreationError.UnknownError)
                }
            } else {
                return@run failure(LobbyCreationError.UserNotFound)
            }
        }
    }

    override fun joinLobby(username: String, lobbyId: Int): LobbyJoinResult {
        return transactionManager.run {
            val userRepo = it.usersRepository
            val lobbyRepo = it.lobbyRepository

            val user = userRepo.getUserByUsername(username)
            if (user != null) {
                val lobby = lobbyRepo.getLobbyById(lobbyId)
                if (lobby != null) {
                    if (lobby.compareUsers(user.id)) {
                        return@run failure(LobbyJoinError.UserAlreadyInLobby)
                    } else if (lobby.isLobbyFull()) {
                        return@run failure(LobbyJoinError.LobbyFull)
                    } else {
                        try {
                            val id = lobbyRepo.joinLobby(user.id, lobbyId)
                            return@run success(id)
                        } catch (e: Exception) {
                            return@run failure(LobbyJoinError.UnknownError)
                        }
                    }
                } else {
                    return@run failure(LobbyJoinError.LobbyNotFound)
                }
            } else {
                return@run failure(LobbyJoinError.UserNotFound)
            }
        }
    }

    override fun getLobbyByUserId(userId: Int): Lobby? {
        TODO("Not yet implemented")
    }

}
