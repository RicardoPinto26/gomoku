package pt.isel.leic.daw.gomokuRoyale.services.game

import pt.isel.leic.daw.gomokuRoyale.utils.Either

sealed class GameCreationError {
    object GameWithThatNameAlreadyExists : GameCreationError()
    object LobbyDoesNotExist : GameCreationError()
    object UserNotInLobby: GameCreationError()
    object UserNotFound: GameCreationError()
    object UnknownError: GameCreationError()
}

typealias GameCreationResult = Either<GameCreationError, Int>

sealed class GameForfeitError {
    object GameDoesNotExist : GameForfeitError()
    object GameAlreadyEnded : GameForfeitError()
    object UserNotInGame : GameForfeitError()
    object UserNotFound: GameForfeitError()
    object UnknownError : GameForfeitError()
}

typealias GameForfeitResult = Either<GameForfeitError, Int>