package pt.isel.leic.daw.gomokuRoyale.services.game

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.domain.BoardDraw
import pt.isel.leic.daw.gomokuRoyale.domain.BoardRun
import pt.isel.leic.daw.gomokuRoyale.domain.BoardWin
import pt.isel.leic.daw.gomokuRoyale.domain.Game
import pt.isel.leic.daw.gomokuRoyale.domain.Opening
import pt.isel.leic.daw.gomokuRoyale.domain.Piece
import pt.isel.leic.daw.gomokuRoyale.domain.Position
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.BoardWrongType
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.InvalidPosition
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.PositionAlreadyPlayed
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserNotInGame
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserWrongTurn
import pt.isel.leic.daw.gomokuRoyale.domain.serializeToJsonString
import pt.isel.leic.daw.gomokuRoyale.repository.TransactionManager
import pt.isel.leic.daw.gomokuRoyale.utils.failure
import pt.isel.leic.daw.gomokuRoyale.utils.success

@Component
class GameServiceImpl(
    private val transactionManager: TransactionManager
) : GameService {

    override fun createGame(lobbyId: Int, userId: Int): GameCreationResult {
        return transactionManager.run {
            val lobbyRepo = it.lobbyRepository
            val lobby = lobbyRepo.getLobbyById(lobbyId) ?: return@run failure(GameCreationError.LobbyDoesNotExist)
            if (!lobby.isLobbyFull()) return@run failure(GameCreationError.LobbyNotFull)
            logger.info("User: $userId - ${lobby.user1.id} - ${lobby.user2?.id}")
            if (!lobby.compareUsers(userId)) return@run failure(GameCreationError.UserNotInLobby)

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
                    lobby.user1.id,
                    lobby.user2.id,
                    newGame.currentOpeningIndex,
                    newGame.board.internalBoard.serializeToJsonString()
                )
                return@run success(
                    newGame.toExternalInfo(gameId, lobbyId)
                )
            } catch (e: Exception) {
                return@run failure(GameCreationError.LobbyNotFull)
            }
        }
    }

    override fun forfeitGame(gameId: Int, userId: Int): GameForfeitResult {
        return transactionManager.run {
            val gameRepo = it.gameRepository
            val lobbyRepo = it.lobbyRepository
            val game = gameRepo.getGameById(gameId) ?: return@run failure(GameForfeitError.GameDoesNotExist)
            val lobbyId =
                lobbyRepo.getLobbyByGameId(gameId)?.id ?: return@run failure(GameForfeitError.GameDoesNotExist)

            if (game.user1.id != userId && game.user2.id != userId) {
                return@run failure(GameForfeitError.UserNotInGame)
            } else if (game.checkGameEnd()) {
                return@run failure(GameForfeitError.GameAlreadyEnded)
            }

            val newGame = game.forfeitGame()
            gameRepo.updateGameWinner(
                gameId,
                (newGame.board as BoardWin).winner.user.id,
                game.board.internalBoard.serializeToJsonString()
            )
            success(
                newGame.toExternalInfo(gameId, lobbyId)
            )

            /**
             * Falta atualizar o lobby para n deixar criar mais jogas com o msm lobbyId; o lobby tem sempre game = null
             */
        }
    }

    override fun playGame(gameId: Int, userId: Int, action: GameAction): GamePlayResult {
        return transactionManager.run {
            val gameRepo = it.gameRepository
            val lobbyRepo = it.lobbyRepository
            var game = gameRepo.getGameById(gameId) ?: return@run failure(GamePlayError.GameDoesNotExist)
            if (game.checkGameEnd()) return@run failure(GamePlayError.GameAlreadyEnded)
            val lobby = lobbyRepo.getLobbyByGameId(gameId) ?: return@run failure(GamePlayError.GameDoesNotExist)
            val user = game.checkUserInGame(userId) ?: return@run failure(GamePlayError.UserNotInGame)
            if ((game.board as BoardRun).turn.user != user) return@run failure(GamePlayError.WrongTurn)

            try {
                when (val nextMove = game.settings.opening.movesList.getOrNull(game.currentOpeningIndex)) {
                    Opening.OpeningMove.CHOOSE_NEXT_MOVE -> {
                        if (action !is GameAction.ChooseMove) {
                            return@run failure(GamePlayError.ChooseCorrectActionNextMove)
                        }
                        game = game.chooseNextMove(action.move, user)
                        gameRepo.updateOpeningVariant(gameId, game.settings.opening.name, game.currentOpeningIndex)
                        gameRepo.updateGamePlayer(
                            gameId,
                            (game.board as BoardRun).player1.user.id,
                            (game.board as BoardRun).player2.user.id,
                            (game.board as BoardRun).turn.user.id,
                            game.currentOpeningIndex
                        )
                    }

                    Opening.OpeningMove.CHOOSE_COLOR -> {
                        if (action !is GameAction.ChooseColor) {
                            return@run failure(GamePlayError.ChooseCorrectActionColor)
                        }
                        game = game.chooseColor(action.color, user)
                        gameRepo.updateGamePlayer(
                            gameId,
                            (game.board as BoardRun).player1.user.id,
                            (game.board as BoardRun).player2.user.id,
                            (game.board as BoardRun).turn.user.id,
                            game.currentOpeningIndex
                        )
                    }

                    Opening.OpeningMove.PLACE_WHITE, Opening.OpeningMove.PLACE_BLACK, null -> {
                        if (action !is GameAction.PlacePiece) {
                            return@run failure(GamePlayError.ChooseCorrectActionPlacePiece)
                        }
                        val piece = when (nextMove) {
                            Opening.OpeningMove.PLACE_WHITE -> Piece.WHITE
                            Opening.OpeningMove.PLACE_BLACK -> Piece.BLACK
                            else ->
                                if ((game.board as BoardRun).player1.user == user) Piece.BLACK else Piece.WHITE
                        }
                        if (nextMove == Opening.OpeningMove.PLACE_WHITE) Piece.WHITE else Piece.BLACK
                        game = game.placePiece(piece, action.position, user)

                        val board = game.board.internalBoard.serializeToJsonString()
                        when (game.board) {
                            is BoardDraw -> gameRepo.updateGameDraw(gameId, board)
                            is BoardRun -> gameRepo.updateGameBoard(
                                gameId,
                                (game.board as BoardRun).turn.user.id,
                                board,
                                game.currentOpeningIndex
                            )

                            is BoardWin -> gameRepo.updateGameWinner(
                                gameId,
                                (game.board as BoardWin).winner.user.id,
                                board
                            )
                        }
                    }

                    else -> {
                        return@run failure(GamePlayError.GameAlreadyEnded)
                    }
                }
                val gameUpdated = gameRepo.getGameDTOById(gameId) ?: return@run failure(GamePlayError.GameDoesNotExist)
                return@run success(gameUpdated.toDTOExternalInfo(lobby))

            } catch (e: Exception) {
                when (e) {
                    is UserNotInGame -> return@run failure(GamePlayError.UserNotInGame)
                    is UserWrongTurn -> return@run failure(GamePlayError.WrongTurn)
                    is BoardWrongType -> return@run failure(GamePlayError.GameAlreadyEnded)
                    is InvalidPosition -> return@run failure(GamePlayError.InvalidPosition)
                    is PositionAlreadyPlayed -> return@run failure(GamePlayError.PositionAlreadyPlayed)
                    // TODO: Add other exceptions
                    else -> throw e
                }
            }
        }
    }

    override fun getGameById(gameId: Int, lobbyId: Int): GameIdentificationResult {
        return transactionManager.run {
            val gameRepository = it.gameRepository
            val lobbyRepository = it.lobbyRepository

            val lobby =
                lobbyRepository.getLobbyById(lobbyId) ?: return@run failure(GameIdentificationError.LobbyDoesNotExist)
            val game =
                gameRepository.getGameDTOById(gameId) ?: return@run failure(GameIdentificationError.GameDoesNotExist)

            return@run success(game.toDTOExternalInfo(lobby))
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GameServiceImpl::class.java)
    }
}

sealed class GameAction {
    data class PlacePiece(val position: Position) : GameAction()
    data class ChooseMove(val move: Opening) : GameAction()
    data class ChooseColor(val color: Piece) : GameAction()
}
