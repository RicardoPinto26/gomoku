package pt.isel.leic.daw.gomokuRoyale.services.lobby

import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.services.ServicesError
import pt.isel.leic.daw.gomokuRoyale.utils.Either

sealed interface LobbyServicesError : ServicesError

sealed class LobbyCreationError : LobbyServicesError {
    object UserNotFound : LobbyCreationError()
}

data class LobbyExternalInfo(
    val id: Int, // lobbyId
    val user1: User,
    val user2: User? = null,
    val gridSize: Int,
    val opening: String,
    val winningLenght: Int,
    val pointsMargin: Int,
    val overflow: Boolean
) {
    constructor(lobby: Lobby) : this(
        lobby.id,
        lobby.user1,
        lobby.user2,
        lobby.settings.boardSize,
        lobby.settings.opening.toString(),
        lobby.settings.winningLength,
        lobby.pointsMargin,
        lobby.settings.overflowAllowed
    )
}

typealias LobbyCreationResult = Either<LobbyCreationError, LobbyExternalInfo>

sealed class LobbyJoinError : LobbyServicesError {
    object LobbyNotFound : LobbyJoinError()
    object UserAlreadyInLobby : LobbyJoinError()
    object UserNotFound : LobbyJoinError()
    object LobbyFull : LobbyJoinError()
}

data class LobbyJoinExternalInfo(
    val usernameCreator: String, // user1
    val usernameJoin: String, // user2
    val lobbyId: Int
)

sealed class LobbySeekError : LobbyServicesError {
    object UserAlreadyInALobby : LobbySeekError()
}

typealias LobbyJoinResult = Either<LobbyJoinError, LobbyJoinExternalInfo>

typealias LobbySeekResult = Either<LobbySeekError, LobbyExternalInfo>
