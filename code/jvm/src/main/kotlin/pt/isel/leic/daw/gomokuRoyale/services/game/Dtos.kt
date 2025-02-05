package pt.isel.leic.daw.gomokuRoyale.services.game

import pt.isel.leic.daw.gomokuRoyale.domain.BoardDraw
import pt.isel.leic.daw.gomokuRoyale.domain.BoardRun
import pt.isel.leic.daw.gomokuRoyale.domain.BoardWin
import pt.isel.leic.daw.gomokuRoyale.domain.Game
import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.domain.serializeToJsonString
import pt.isel.leic.daw.gomokuRoyale.domain.user.GameDTO
import pt.isel.leic.daw.gomokuRoyale.services.ServicesError
import pt.isel.leic.daw.gomokuRoyale.services.users.UserExternalInfo
import pt.isel.leic.daw.gomokuRoyale.services.users.toExternalInfo
import pt.isel.leic.daw.gomokuRoyale.utils.Either

sealed interface GameServicesError : ServicesError

sealed class GameCreationError : GameServicesError {
    // object GameWithThatNameAlreadyExists : GameCreationError()
    object LobbyDoesNotExist : GameCreationError()
    object LobbyAlreadyHasGame : GameCreationError()
    object LobbyNotFull : GameCreationError()
    object UserNotInLobby : GameCreationError()
}

typealias GameCreationResult = Either<GameCreationError, GameExternalInfo>

/**
--------------------------------------------------------------------------
 */

sealed class GameForfeitError : GameServicesError {
    object GameDoesNotExist : GameForfeitError()
    object GameAlreadyEnded : GameForfeitError()
    object UserNotInGame : GameForfeitError()
}

typealias GameForfeitResult = Either<GameForfeitError, GameExternalInfo>

/**
--------------------------------------------------------------------------
 */

sealed class GamePlayError : GameServicesError {
    object GameDoesNotExist : GamePlayError()
    object GameAlreadyEnded : GamePlayError()
    object UserNotInGame : GamePlayError()
    object PositionAlreadyPlayed : GamePlayError()
    object InvalidPosition : GamePlayError()
    object InvalidNextMove : GamePlayError()
    object InvalidColor : GamePlayError()
    object ChooseCorrectActionColor : GamePlayError()
    object ChooseCorrectActionNextMove : GamePlayError()
    object ChooseCorrectActionPlacePiece : GamePlayError()

    object WrongTurn : GamePlayError()
}

data class GameExternalInfo(
    val id: Int,
    val lobbyID: Int,
    val user1: UserExternalInfo,
    val user2: UserExternalInfo,
    val board: String,
    val turn: String?,
    val status: String,
    val winner: UserExternalInfo? = null
)

fun Game.toExternalInfo(id: Int, lobbyID: Int) = GameExternalInfo(
    id,
    lobbyID,
    user1.toExternalInfo(),
    user2.toExternalInfo(),
    board.internalBoard.serializeToJsonString(),
    when (board) {
        is BoardDraw -> null
        is BoardRun -> board.turn.user.username
        is BoardWin -> null
    },
    when (board) {
        is BoardDraw -> "DRAW"
        is BoardRun -> "PLAYING"
        is BoardWin -> "WON"
    },
    if (board is BoardWin) board.winner.user.toExternalInfo() else null
)

fun GameDTO.toExternalInfo(lobby: Lobby) = GameExternalInfo(
    id,
    lobbyId,
    lobby.user1.toExternalInfo(),
    lobby.user2!!.toExternalInfo(),
    board,
    turn.toString(),
    state,
    when (winner) {
        lobby.user1.id -> lobby.user1.toExternalInfo()
        lobby.user2.id -> lobby.user2.toExternalInfo()
        else -> null
    }
)

typealias GamePlayResult = Either<GamePlayError, GameDTOExternalInfo>

/**
--------------------------------------------------------------------------
 */

data class GameDTOExternalInfo(
    val id: Int,
    val lobbyID: Int,
    val blackPlayer: UserExternalInfo,
    val whitePlayer: UserExternalInfo,
    val board: String,
    val turn: UserExternalInfo,
    val openingIndex: Int,
    val openingVariant: String?,
    val status: String,
    val winner: UserExternalInfo? = null
)

fun GameDTO.toDTOExternalInfo(lobby: Lobby) = GameDTOExternalInfo(
    id,
    lobbyId,
    when (blackPlayer) {
        lobby.user1.id -> lobby.user1.toExternalInfo()
        lobby.user2!!.id -> lobby.user2.toExternalInfo()
        else -> {
            throw Exception("Invalid black player")
        }
    },
    when (whitePlayer) {
        lobby.user1.id -> lobby.user1.toExternalInfo()
        lobby.user2!!.id -> lobby.user2.toExternalInfo()
        else -> {
            throw Exception("Invalid black player")
        }
    },
    board,
    when (turn) {
        lobby.user1.id -> lobby.user1.toExternalInfo()
        lobby.user2!!.id -> lobby.user2.toExternalInfo()
        else -> {
            throw Exception("Invalid turn player")
        }
    },
    openingIndex,
    openingVariant,
    state,
    when (winner) {
        lobby.user1.id -> lobby.user1.toExternalInfo()
        lobby.user2!!.id -> lobby.user2.toExternalInfo()
        else -> null
    }
)

sealed class GameIdentificationError : GameServicesError {
    object GameDoesNotExist : GameIdentificationError()
    object LobbyDoesNotExist : GameIdentificationError()
}

typealias GameIdentificationResult = Either<GameIdentificationError, GameDTOExternalInfo>
