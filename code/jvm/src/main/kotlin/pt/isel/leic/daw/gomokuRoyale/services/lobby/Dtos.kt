package pt.isel.leic.daw.gomokuRoyale.services.lobby

import pt.isel.leic.daw.gomokuRoyale.utils.Either

sealed class LobbyCreationError {
    object UserNotFound : LobbyCreationError()
    object UnknownError : LobbyCreationError()
}

typealias LobbyCreationResult = Either<LobbyCreationError, Int>

sealed class LobbyJoinError {
    object LobbyNotFound : LobbyJoinError()
    object UserAlreadyInLobby : LobbyJoinError()
    object UserNotFound : LobbyJoinError()
    object LobbyFull : LobbyJoinError()
    object UnknownError : LobbyJoinError()
}

typealias LobbyJoinResult = Either<LobbyJoinError, Int>
