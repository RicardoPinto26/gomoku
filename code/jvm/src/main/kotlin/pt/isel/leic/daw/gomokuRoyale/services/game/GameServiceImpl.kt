package pt.isel.leic.daw.gomokuRoyale.services.game

import pt.isel.leic.daw.gomokuRoyale.domain.BoardWin
import pt.isel.leic.daw.gomokuRoyale.domain.Game
import pt.isel.leic.daw.gomokuRoyale.repository.TransactionManager
import pt.isel.leic.daw.gomokuRoyale.utils.failure
import pt.isel.leic.daw.gomokuRoyale.utils.success

//@Component
class GameServiceImpl(
    private val transactionManager: TransactionManager,
    private val gameDomain: Game
) : GameService {
    override fun createGame(name: String, user1: Int, user2: Int, lobbyId: Int): GameCreationResult {
        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository
            val lobby = lobbyRepo.getLobbyById(lobbyId)
            if (lobby != null) {
                if (lobby.compareUsers(user1) && lobby.compareUsers(user2)) {
                    return@run failure(GameCreationError.UserNotInLobby)
                }
            } else {
                return@run failure(GameCreationError.LobbyDoesNotExist)
            }

            val gameRepo = it.gameRepository
            try {
                val id = gameRepo.createGame(name, user1, user2, lobbyId)
                return@run success(id)
            } catch (e: Exception) {
                return@run failure(GameCreationError.UnknownError)
            }
        }
    }

    override fun forfeitGame(gameId: Int, user: Int): GameForfeitResult {
        return transactionManager.run {
            val gameRepo = it.gameRepository
            val game = gameRepo.getGameById(gameId)

            if (game != null) {
                if (game.user1.id != user && game.user2.id != user) {
                    return@run failure(GameForfeitError.UserNotInGame)
                } else if (game.checkGameEnd()) {
                    return@run failure(GameForfeitError.GameAlreadyEnded)
                }
            } else {
                return@run failure(GameForfeitError.GameDoesNotExist)
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

    override fun getGameByLobbyId(lobbyId: Int): Int? {
        TODO("Not yet implemented")
    }
}