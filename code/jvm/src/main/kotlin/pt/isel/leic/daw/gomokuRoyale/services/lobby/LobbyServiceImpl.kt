package pt.isel.leic.daw.gomokuRoyale.services.lobby

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.domain.BoardRun
import pt.isel.leic.daw.gomokuRoyale.domain.Game
import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.domain.serializeToJsonString
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.repository.TransactionManager
import pt.isel.leic.daw.gomokuRoyale.utils.failure
import pt.isel.leic.daw.gomokuRoyale.utils.success

@Component
class LobbyServiceImpl(
    private val transactionManager: TransactionManager
) : LobbyService {
    companion object {
        private val logger = LoggerFactory.getLogger(LobbyServiceImpl::class.java)
    }

    override fun createLobby(
        user: User,
        name: String,
        gridSize: Int,
        opening: String,
        winningLenght: Int,
        pointsMargin: Int,
        overflow: Boolean
    ): LobbyCreationResult {
        logger.info("Creating Lobby")
        logger.info("$gridSize")
        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository
            val id = lobbyRepo.createLobby(name, user.id, gridSize, opening, winningLenght, pointsMargin, overflow)
            return@run success(
                LobbyExternalInfo(
                    id = id,
                    user1 = user,
                    gridSize = gridSize,
                    opening = opening,
                    winningLength = winningLenght,
                    pointsMargin = pointsMargin,
                    overflow = overflow
                )
            )
        }
    }

    override fun joinLobby(user: User, lobbyId: Int): LobbyJoinResult {
        logger.info("Joining lobby $lobbyId")
        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository
            val gameRepo = it.gameRepository
            val lobby = lobbyRepo.getLobbyById(lobbyId) ?: return@run failure(LobbyJoinError.LobbyNotFound)
            if (lobby.compareUsers(user.id)) return@run failure(LobbyJoinError.UserAlreadyInLobby)
            if (lobby.isLobbyFull()) return@run failure(LobbyJoinError.LobbyFull)
            val id = lobbyRepo.joinLobby(user.id, lobbyId)

            val newLobby = lobbyRepo.getLobbyById(id)!!

            val newGame = Game(
                lobby.name,
                lobby.user1,
                user,
                lobby.settings
            )

            val gameId = gameRepo.createGame(
                newLobby.id,
                (newGame.board as BoardRun).turn.user.id,
                newLobby.user1.id,
                newLobby.user2!!.id,
                newGame.board.internalBoard.serializeToJsonString()
            )

            return@run success(
                LobbyJoinExternalInfo(
                    usernameCreator = newLobby.user1.username,
                    usernameJoin = newLobby.user2.username,
                    lobbyId = id,
                    gameId = gameId
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
        overflow: Boolean,
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
                overflow,
                userRating - pointsMargin,
                userRating + pointsMargin
            )
            if (lobbyID != null) {
                lobbyRepo.joinLobby(user.id, lobbyID)
                val lobby = lobbyRepo.getLobbyById(lobbyID)!!
                return@run success(LobbyExternalInfo(lobby))
            }
            val createdLobbyID = lobbyRepo.createLobby(
                "Seeked Lobby",
                user.id,
                gridSize,
                opening,
                winningLength,
                pointsMargin,
                overflow
            )
            return@run success(LobbyExternalInfo(lobbyRepo.getLobbyById(createdLobbyID)!!))
        }
    }

    override fun getAvailableLobbies(user: User): LobbiesAvailableResult {
        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository
            val lobbies = lobbyRepo.getAvailableLobbies()
            return@run success(LobbiesAvailableExternalInfo(lobbies.map { lobby -> LobbyExternalInfo(lobby) }))
        }
    }

    override fun getLobbyDetails(user: User, lobbyId: Int): LobbyDetailsResult {
        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository

            val lobby = lobbyRepo.getLobbyById(lobbyId) ?: return@run failure(LobbyDetailsError.LobbyNotFound)

            return@run success(LobbyExternalInfo(lobby))
        }
    }
}
