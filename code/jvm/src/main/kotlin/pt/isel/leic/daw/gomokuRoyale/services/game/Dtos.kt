package pt.isel.leic.daw.gomokuRoyale.services.game

import pt.isel.leic.daw.gomokuRoyale.domain.Board
import pt.isel.leic.daw.gomokuRoyale.domain.Position
import pt.isel.leic.daw.gomokuRoyale.utils.Either

sealed class GameCreationError {
    //object GameWithThatNameAlreadyExists : GameCreationError()
    object LobbyDoesNotExist : GameCreationError()
    object LobbyAlreadyHasGame : GameCreationError()
    object LobbyNotFull : GameCreationError()
    object UnknownError : GameCreationError()
    object UserNotInLobby : GameCreationError()
}

data class GameCreationExternalInfo(
    val id: Int,
    val name: String,
    val user1: String,
    val user2: String,
    val board: Board,
    val lobbyId: Int
)

typealias GameCreationResult = Either<GameCreationError, GameCreationExternalInfo>

/**
 --------------------------------------------------------------------------
 */

sealed class GameForfeitError {
    object GameDoesNotExist : GameForfeitError()
    object GameAlreadyEnded : GameForfeitError()
    object UserNotInGame : GameForfeitError()
    object UnknownError : GameForfeitError()
}

data class GameForfeitExternalInfo(
    val winner: String
)

typealias GameForfeitResult = Either<GameForfeitError, GameForfeitExternalInfo>

/**
--------------------------------------------------------------------------
 */

sealed class GamePlayError {
    object GameDoesNotExist : GamePlayError()
    object GameAlreadyEnded : GamePlayError()
    object UserNotInGame : GamePlayError()
    object PositionAlreadyPlayed : GamePlayError()
    object InvalidPosition : GamePlayError()
    object UnknownError : GamePlayError()
}

data class GamePlayExternalInfo(
    val lastMove: Position,
    val username: String,
    val board: String,
)

typealias GamePlayResult = Either<GamePlayError, GamePlayExternalInfo>

/**
--------------------------------------------------------------------------
 */


sealed class GameIdentificationError {
    object GameDoesNotExist : GameIdentificationError()
}

data class GameIdentificationExternalInfo(
    val id: Int,
    val name: String,
    val user1: String,
    val user2: String,
    val board: Board
)

typealias GameIdentificationResult = Either<GameIdentificationError, GameIdentificationExternalInfo>