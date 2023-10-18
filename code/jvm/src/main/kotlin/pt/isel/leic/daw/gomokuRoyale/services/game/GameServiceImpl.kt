package pt.isel.leic.daw.gomokuRoyale.services.game

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.domain.*
import pt.isel.leic.daw.gomokuRoyale.repository.TransactionManager
import pt.isel.leic.daw.gomokuRoyale.utils.failure
import pt.isel.leic.daw.gomokuRoyale.utils.success

@Component
class GameServiceImpl(
    private val transactionManager: TransactionManager,
) : GameService {

    override fun createGame(lobbyId: Int, userId: Int): GameCreationResult {
        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository
            val lobby = lobbyRepo.getLobbyById(lobbyId) ?: return@run failure(GameCreationError.LobbyDoesNotExist)
            if (!lobby.isLobbyFull()) return@run failure(GameCreationError.LobbyNotFull)
            logger.info("User: $userId - ${lobby.user1.id} - ${lobby.user2?.id}")
            if (!lobby.compareUsers(userId)) return@run failure(GameCreationError.UserNotInLobby)
            //if (!lobby.isLobbyStarted()) return@run failure(GameCreationError.LobbyAlreadyHasGame)

            val newGame = Game(
                lobby.name,
                lobby.user1,
                lobby.user2 ?: return@run failure(GameCreationError.LobbyNotFull),
                lobby.settings
            )

            val gameRepo = it.gameRepository
            try {
                val gameId = gameRepo.createGame(
                    lobbyId,
                    (newGame.board as BoardRun).turn.user.id,
                    newGame.board.internalBoard.serializeToJsonString()
                )
                return@run success(
                    GameCreationExternalInfo(
                        gameId,
                        newGame.name,
                        newGame.user1.username,
                        newGame.user2.username,
                        newGame.board,
                        lobbyId
                    )
                )
            } catch (e: Exception) {
                return@run failure(GameCreationError.UnknownError)
            }
        }
    }


    override fun forfeitGame(gameId: Int, userId: Int): GameForfeitResult {
        return transactionManager.run {
            val gameRepo = it.gameRepository
            val game = gameRepo.getGameById(gameId) ?: return@run failure(GameForfeitError.GameDoesNotExist)

            if (game.user1.id != userId && game.user2.id != userId) {
                return@run failure(GameForfeitError.UserNotInGame)
            } else if (game.checkGameEnd()) {
                return@run failure(GameForfeitError.GameAlreadyEnded)
            }

            val newGame = game.forfeitGame()
            try {
                gameRepo.updateGameWinner(gameId, (newGame.board as BoardWin).winner.user.id)
                success(
                    GameForfeitExternalInfo(
                        (newGame.board).winner.user.username
                    )
                )

                /**
                 * Falta atualizar o lobby para n deixar criar mais jogas com o msm lobbyId; o lobby tem sempre game = null
                 */
            } catch (e: Exception) {
                failure(GameForfeitError.UnknownError)
            }
        }
    }


    override fun playGame(gameId: Int, userId: Int, position: Position): GamePlayResult {
        return transactionManager.run {
            val gameRepo = it.gameRepository
            logger.info("--- --- GameId: $gameId -------")
            var game = gameRepo.getGameById(gameId) ?: return@run failure(GamePlayError.GameDoesNotExist)
            if (game.checkGameEnd()) return@run failure(GamePlayError.GameAlreadyEnded)
            val user = game.checkUserInGame(userId) ?: return@run failure(GamePlayError.UserNotInGame)
            val piece = if(game.user1.id == userId) Piece.BLACK else Piece.WHITE // n é o melhor e só dá para o FREESTYLE

            //try {
                game = game.placePiece(piece, position, user)
                val board = game.board.internalBoard.serializeToJsonString()
                val turn = game.otherTurn(userId)

                gameRepo.updateGameBoard(gameId,turn.id, board)
                return@run success(
                    GamePlayExternalInfo(
                        position,
                        user.username,
                        board
                    )
                )
            //} catch (e: Exception) {
            //    logger.info("Error: ${e.message}")
            //    failure(GamePlayError.UnknownError)
            //}
        }
    }


    /*override fun getGameById(gameId: Int): GameIdentificationResult {
        return transactionManager.run {
            val gameRepo = it.gameRepository
            val g = gameRepo.getGameById(gameId) ?: return@run failure(GameIdentificationError.GameDoesNotExist)
            return@run success(
                GameIdentificationExternalInfo(
                    gameId,
                    g.name,
                    g.user1.username,
                    g.user2.username,
                    g.board
                )
            )
        }
    }
     */

    companion object {
        private val logger = LoggerFactory.getLogger(GameServiceImpl::class.java)
    }
}
