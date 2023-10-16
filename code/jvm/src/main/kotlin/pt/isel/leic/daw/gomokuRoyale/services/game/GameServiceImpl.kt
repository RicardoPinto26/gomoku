package pt.isel.leic.daw.gomokuRoyale.services.game

import pt.isel.leic.daw.gomokuRoyale.domain.BoardWin
import pt.isel.leic.daw.gomokuRoyale.domain.Game
import pt.isel.leic.daw.gomokuRoyale.repository.TransactionManager
import pt.isel.leic.daw.gomokuRoyale.services.users.UsersServiceImpl
import pt.isel.leic.daw.gomokuRoyale.utils.failure
import pt.isel.leic.daw.gomokuRoyale.utils.success

//@Component
class GameServiceImpl(
    private val transactionManager: TransactionManager,
    private val usersService: UsersServiceImpl,
    private val gameDomain: Game
) : GameService {
    override fun createGame(name: String, tokenUser1: String, tokenUser2: String, lobbyId: Int): GameCreationResult {
        val userService = usersService
        val user1 = userService.getUserByToken(tokenUser1) ?: return failure(GameCreationError.UserNotFound)
        val user2 = userService.getUserByToken(tokenUser2) ?: return failure(GameCreationError.UserNotFound)

        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository
            val lobby = lobbyRepo.getLobbyById(lobbyId) ?: return@run failure(GameCreationError.LobbyDoesNotExist)
            if (lobby.compareUsers(user1.id) && lobby.compareUsers(user2.id)) {
                return@run failure(GameCreationError.UserNotInLobby)
            }

            val gameRepo = it.gameRepository
            try {
                val id = gameRepo.createGame(name, user1.id, user2.id, lobbyId)
                return@run success(id)
            } catch (e: Exception) {
                return@run failure(GameCreationError.UnknownError)
            }
        }
    }

    override fun forfeitGame(gameId: Int, token: String): GameForfeitResult {
        val userService = usersService
        val user = userService.getUserByToken(token) ?: return failure(GameForfeitError.UserNotFound)
        return transactionManager.run {
            val gameRepo = it.gameRepository
            val game = gameRepo.getGameById(gameId) ?: return@run failure(GameForfeitError.GameDoesNotExist)

            if (game.user1.id != user.id && game.user2.id != user.id) {
                return@run failure(GameForfeitError.UserNotInGame)
            } else if (game.checkGameEnd()) {
                return@run failure(GameForfeitError.GameAlreadyEnded)
            }

            val gameStateRepo = it.gameStateRepository
            val newGame = gameDomain.forfeitGame()
            try {
                val id = gameStateRepo.updateGameStateWinner(gameId, (newGame.board as BoardWin).winner.user.id)
                success(id)
            } catch (e: Exception) {
                failure(GameForfeitError.UnknownError)
            }
        }
    }

    override fun getGameByLobbyId(lobbyId: Int): Game? {
        return transactionManager.run {
            val gameRepo = it.gameRepository
            gameRepo.getGameByLobbyId(lobbyId) ?: return@run null
        }
    }
}