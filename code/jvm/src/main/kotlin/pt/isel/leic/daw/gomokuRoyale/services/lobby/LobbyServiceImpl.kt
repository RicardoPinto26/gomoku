package pt.isel.leic.daw.gomokuRoyale.services.lobby

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.repository.jdbi.JdbiTransactionManager
import pt.isel.leic.daw.gomokuRoyale.services.users.UserServiceImpl
import pt.isel.leic.daw.gomokuRoyale.utils.failure
import pt.isel.leic.daw.gomokuRoyale.utils.success

@Component
class LobbyServiceImpl(
    private val transactionManager: JdbiTransactionManager,
    private val usersService: UserServiceImpl
) : LobbyService {
    companion object{
        private val logger = LoggerFactory.getLogger(LobbyServiceImpl::class.java)
    }
    override fun createLobby(
        token: String,
        gridSize: Int,
        opening: String,
        variant: String,
        pointsMargin: Int
    ): LobbyCreationResult {
        logger.info("Creating Lobby")
        logger.info("$gridSize")
        val userService = usersService
        val user = userService.getUserByToken(token) ?: return failure(LobbyCreationError.UserNotFound)

        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository
            val id = lobbyRepo.createLobby(user.id, gridSize, opening, variant, pointsMargin)
            return@run success(
                LobbyExternalInfo(
                    id = id,
                    user1 = user,
                    gridSize = gridSize,
                    opening = opening,
                    variant = variant,
                    pointsMargin = pointsMargin
                )
            )
        }
    }

    override fun joinLobby(token: String, lobbyId: Int): LobbyJoinResult {
        logger.info("Joining lobby $lobbyId")
        val userService = usersService
        val user = userService.getUserByToken(token) ?: return failure(LobbyJoinError.UserNotFound)
        logger.info("Got the user")

        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository
            logger.info("lobbyRepo: $lobbyRepo")
            val lobby = lobbyRepo.getLobbyById(lobbyId) ?: return@run failure(LobbyJoinError.LobbyNotFound)
            logger.info("lobby : $lobby")
            if (lobby.compareUsers(user.id)) return@run failure(LobbyJoinError.UserAlreadyInLobby)
            if (lobby.isLobbyFull()) return@run failure(LobbyJoinError.LobbyFull)

            val id = lobbyRepo.joinLobby(user.id, lobbyId)
            return@run success(
                LobbyJoinExternalInfo(
                    usernameCreator = lobby.user1.username,
                    usernameJoin = user.username,
                    lobbyId = id
                )
            )
        }
    }

    override fun getLobbyByUserToken(token: String): Lobby? {
        TODO("Not yet implemented")
    }
}
