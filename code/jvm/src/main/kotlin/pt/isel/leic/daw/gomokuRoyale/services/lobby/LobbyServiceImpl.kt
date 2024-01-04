package pt.isel.leic.daw.gomokuRoyale.services.lobby

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.domain.BoardRun
import pt.isel.leic.daw.gomokuRoyale.domain.Game
import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.domain.serializeToJsonString
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.repository.TransactionManager
import pt.isel.leic.daw.gomokuRoyale.services.game.toExternalInfo
import pt.isel.leic.daw.gomokuRoyale.services.users.toPublicExternalInfo
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
        winningLength: Int,
        pointsMargin: Int,
        overflow: Boolean
    ): LobbyCreationResult {
        logger.info("Creating Lobby")
        logger.info("$gridSize")
        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository
            val lobbies = lobbyRepo.getUserLobbys(user.id)
            if (!lobbies.all { lobby -> lobby.isGameFinished() }) {
                return@run failure(LobbyCreationError.UserAlreadyInALobby(lobbies.first { lobby -> !lobby.isGameFinished() }.id))
            }
            val id = lobbyRepo.createLobby(name, user.id, gridSize, opening, winningLength, pointsMargin, overflow)
            return@run success(
                LobbyExternalInfo(
                    id = id,
                    user1 = user,
                    gridSize = gridSize,
                    opening = opening,
                    winningLength = winningLength,
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

            val lobbies = lobbyRepo.getUserLobbys(user.id)
            if (!lobbies.all { lobby -> lobby.isGameFinished() }) {
                return@run failure(LobbyJoinError.UserAlreadyInALobby(lobbies.first { lobby -> !lobby.isGameFinished() }.id))
            }

            val lobby = lobbyRepo.getLobbyById(lobbyId) ?: return@run failure(LobbyJoinError.LobbyNotFound)
            if (lobby.compareUsers(user.id)) return@run failure(LobbyJoinError.UserAlreadyInLobby)
            if (lobby.isLobbyFull()) return@run failure(LobbyJoinError.LobbyFull)
            lobbyRepo.joinLobby(user.id, lobbyId)

            val newLobby = lobbyRepo.getLobbyById(lobbyId) ?: return@run failure(LobbyJoinError.LobbyNotFound)

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
                newGame.currentOpeningIndex,
                newGame.board.internalBoard.serializeToJsonString()
            )

            return@run success(
                LobbyJoinExternalInfo(
                    usernameCreator = newLobby.user1.username,
                    usernameJoin = newLobby.user2.username,
                    lobbyId = lobbyId,
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
            val gameRepo = it.gameRepository

            val lobbies = lobbyRepo.getUserLobbys(user.id)
            println(lobbies)

            if (!lobbies.all { lobby -> lobby.isGameFinished() }) {
                return@run failure(LobbySeekError.UserAlreadyInALobby(lobbies.first { lobby -> !lobby.isGameFinished() }.id))
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

                val newGame = Game(
                    lobby.name,
                    lobby.user1,
                    user,
                    lobby.settings
                )

                val gameId = gameRepo.createGame(
                    lobby.id,
                    (newGame.board as BoardRun).turn.user.id,
                    lobby.user1.id,
                    lobby.user2!!.id,
                    newGame.currentOpeningIndex,
                    newGame.board.internalBoard.serializeToJsonString()
                )
                return@run success(
                    PublicLobbyExternalInfo(
                        lobby,
                        gameRepo.getGameById(gameId)?.toExternalInfo(gameId, lobby.id)
                        // newGame.toExternalInfo(gameId, lobby.id)
                    )
                )
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
            return@run success(
                PublicLobbyExternalInfo(
                    createdLobbyID,
                    "Seeked Lobby",
                    user.toPublicExternalInfo(),
                    null,
                    gridSize,
                    opening,
                    winningLength,
                    pointsMargin,
                    overflow,
                    null
                )
            )
        }
    }

    override fun getAvailableLobbies(user: User): LobbiesAvailableResult {
        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository
            val lobbies = lobbyRepo.getAvailableLobbies()
            return@run success(
                LobbiesAvailableExternalInfo(
                    lobbies
                        .map { lobby -> PublicLobbyExternalInfo(lobby) }
                        .filter { lobby -> lobby.user1.username != user.username && !lobby.isLobbyStarted() }
                )
            )
        }
    }

    override fun getLobbyDetails(user: User, lobbyId: Int): LobbyDetailsResult {
        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository
            val gameRepo = it.gameRepository

            val lobby = lobbyRepo.getLobbyById(lobbyId) ?: return@run failure(LobbyDetailsError.LobbyNotFound)
            val game = gameRepo.getGameByLobbyId(lobby.id)
            logger.info("Game: $game")
            return@run success(PublicLobbyExternalInfo(lobby, game?.toExternalInfo(lobby)))
        }
    }
}
