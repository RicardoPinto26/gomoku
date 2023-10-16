package pt.isel.leic.daw.gomokuRoyale.services.lobby

import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.repository.jdbi.JdbiTransactionManager
import pt.isel.leic.daw.gomokuRoyale.services.users.UsersServiceImpl
import pt.isel.leic.daw.gomokuRoyale.utils.failure
import pt.isel.leic.daw.gomokuRoyale.utils.success

@Component
class LobbyServiceImpl(
    private val transactionManager: JdbiTransactionManager,
    private val usersService: UsersServiceImpl
) : LobbyServiceInterface {

    override fun createLobby(
        token: String, gridSize: Int, opening: String, variant: String, pointsMargin: Int
    ): LobbyCreationResult {
        val userService = usersService
        val user = userService.getUserByToken(token) ?: return failure(LobbyCreationError.UserNotFound)

        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository
            try {
                val id = lobbyRepo.createLobby(user.id, gridSize, opening, variant, pointsMargin)
                return@run  success(
                    LobbyExternalInfo(
                        id = id,
                        user1 = user,
                        gridSize = gridSize,
                        opening = opening,
                        variant = variant,
                        pointsMargin = pointsMargin
                    )
                )
            } catch (e: Exception) {
                return@run failure(LobbyCreationError.UnknownError)
            }
        }
    }

    override fun joinLobby(token: String, lobbyId: Int): LobbyJoinResult {
        val userService = usersService
        val user = userService.getUserByToken(token) ?: return failure(LobbyJoinError.UserNotFound)

        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository
            val lobby = lobbyRepo.getLobbyById(lobbyId) ?: return@run failure(LobbyJoinError.LobbyNotFound)
            if (lobby.compareUsers(user.id)) return@run failure(LobbyJoinError.UserAlreadyInLobby)
            if (lobby.isLobbyFull()) return@run failure(LobbyJoinError.LobbyFull)

            try {
                val id = lobbyRepo.joinLobby(user.id, lobbyId)
                return@run success(
                    LobbyJoinExternalInfo(
                        usernameCreator = lobby.user1.username,
                        usernameJoin = user.username,
                        lobbyId = id
                    )
                )
            } catch (e: Exception) {
                return@run failure(LobbyJoinError.UnknownError)
            }
        }
    }

    override fun getLobbyByUserToken(token: String): Lobby? {
        TODO("Not yet implemented")
    }

}
