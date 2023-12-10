package pt.isel.leic.daw.gomokuRoyale.services.lobby

import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.services.ServicesError
import pt.isel.leic.daw.gomokuRoyale.services.users.PublicUserExternalInfo
import pt.isel.leic.daw.gomokuRoyale.services.users.toPublicExternalInfo
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
    val winningLength: Int,
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

// Lobby Join
sealed class LobbyJoinError : LobbyServicesError {
    object LobbyNotFound : LobbyJoinError()
    object UserAlreadyInLobby : LobbyJoinError()
    object UserNotFound : LobbyJoinError()
    object LobbyFull : LobbyJoinError()
}

data class LobbyJoinExternalInfo(
    val usernameCreator: String, // user1
    val usernameJoin: String, // user2
    val lobbyId: Int,
    val gameId: Int
)

typealias LobbyJoinResult = Either<LobbyJoinError, LobbyJoinExternalInfo>

// Lobby Seek
data class LobbySeekExternalInfo(
    val usernameCreator: String, // user1
    val usernameJoin: String? = null, // user2
    val lobbyId: Int,
    val gameId: Int? = null,
    val gridSize: Int,
    val opening: String,
    val winningLength: Int,
    val pointsMargin: Int,
    val overflow: Boolean
) {
    constructor(lobby: Lobby, gameId: Int?) : this(
        lobby.user1.username,
        lobby.user2?.username,
        lobby.id,
        gameId,
        lobby.settings.boardSize,
        lobby.settings.opening.toString(),
        lobby.settings.winningLength,
        lobby.pointsMargin,
        lobby.settings.overflowAllowed
    )
}

sealed class LobbySeekError : LobbyServicesError {
    object UserAlreadyInALobby : LobbySeekError()
    object UserAlreadyInAGame : LobbySeekError()
}

typealias LobbySeekResult = Either<LobbySeekError, LobbySeekExternalInfo>

// Get Lobbies
data class LobbiesAvailableExternalInfo(
    val lobbies: List<PublicLobbyExternalInfo>
)

sealed class GetLobbiesError : LobbyServicesError {
    object UserNotFound : GetLobbiesError()
}

typealias LobbiesAvailableResult = Either<GetLobbiesError, LobbiesAvailableExternalInfo>

// Get Lobby Details
data class PublicLobbyExternalInfo(
    val id: Int,
    // val gameId: Int? = null,
    val user1: PublicUserExternalInfo,
    val user2: PublicUserExternalInfo? = null,
    val gridSize: Int,
    val opening: String,
    val winningLength: Int,
    val pointsMargin: Int,
    val overflow: Boolean

) {
    constructor(lobby: Lobby) : this(
        lobby.id,
        // lobby.game,
        lobby.user1.toPublicExternalInfo(),
        lobby.user2?.toPublicExternalInfo(),
        lobby.settings.boardSize,
        lobby.settings.opening.toString(),
        lobby.settings.winningLength,
        lobby.pointsMargin,
        lobby.settings.overflowAllowed
    )
}

sealed class LobbyDetailsError : LobbyServicesError {
    object LobbyNotFound : LobbyDetailsError()
}
typealias LobbyDetailsResult = Either<LobbyDetailsError, PublicLobbyExternalInfo>
