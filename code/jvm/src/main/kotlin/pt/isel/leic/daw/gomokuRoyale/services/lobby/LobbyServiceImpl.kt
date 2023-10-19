package pt.isel.leic.daw.gomokuRoyale.services.lobby

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.domain.BoardRun
import pt.isel.leic.daw.gomokuRoyale.domain.Game
import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.domain.serializeToJsonString
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.repository.TransactionManager
import pt.isel.leic.daw.gomokuRoyale.services.users.UserService
import pt.isel.leic.daw.gomokuRoyale.utils.failure
import pt.isel.leic.daw.gomokuRoyale.utils.success

@Component
class LobbyServiceImpl(
    private val transactionManager: TransactionManager,
    private val usersService: UserService
) : LobbyService {
    companion object {
        private val logger = LoggerFactory.getLogger(LobbyServiceImpl::class.java)
    }

    override fun createLobby(
        name: String,
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
            val id = lobbyRepo.createLobby(name, user.id, gridSize, opening, variant, pointsMargin)
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

    override fun seekLobby(
        user: User,
        gridSize: Int,
        winningLength: Int,
        opening: String,
        pointsMargin: Int
    ): LobbySeekResult {
        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository

            if (lobbyRepo.getUserLobbys(user.id).any { lobby -> !lobby.isGameFinished() }
            ) {
                return@run failure(LobbySeekError.UserAlreadyInALobby)
            }

            val userRating = user.rating.toInt()
            val lobbyID: Int? = lobbyRepo.seekLobbyID(
                userRating,
                gridSize,
                winningLength,
                opening,
                userRating - pointsMargin,
                userRating + pointsMargin
            )
            if (lobbyID != null) {
                lobbyRepo.joinLobby(user.id, lobbyID)
                val gameRepo = it.gameRepository
                val lobby = lobbyRepo.getLobbyById(lobbyID)!!
                val game = Game(
                    lobby.name,
                    lobby.user1,
                    lobby.user2!!,
                    lobby.settings
                )
                gameRepo.createGame(
                    lobbyID,
                    (game.board as BoardRun).turn.user.id,
                    game.board.internalBoard.serializeToJsonString()
                )
                return@run success(LobbyExternalInfo(lobby))
            }
            val createdLobbyID = lobbyRepo.createLobby("Seeked Lobby", user.id, gridSize, opening, "???", pointsMargin)
            return@run success(LobbyExternalInfo(lobbyRepo.getLobbyById(createdLobbyID)!!))
        }
    }
}
