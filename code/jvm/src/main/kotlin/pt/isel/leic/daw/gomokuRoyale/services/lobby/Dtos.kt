package pt.isel.leic.daw.gomokuRoyale.services.lobby

import pt.isel.leic.daw.gomokuRoyale.domain.Lobby
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.services.ServicesError
import pt.isel.leic.daw.gomokuRoyale.services.game.GameExternalInfo
import pt.isel.leic.daw.gomokuRoyale.services.users.PublicUserExternalInfo
import pt.isel.leic.daw.gomokuRoyale.services.users.toPublicExternalInfo
import pt.isel.leic.daw.gomokuRoyale.utils.Either

sealed interface LobbyServicesError : ServicesError

sealed class LobbyCreationError : LobbyServicesError {
    object UserNotFound : LobbyCreationError()
    class UserAlreadyInALobby(val lobbyID: Int) : LobbyCreationError()
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

    class UserAlreadyInALobby(val lobbyID: Int) : LobbyJoinError()
}

data class LobbyJoinExternalInfo(
    val usernameCreator: String, // user1
    val usernameJoin: String, // user2
    val lobbyId: Int,
    val gameId: Int
)

typealias LobbyJoinResult = Either<LobbyJoinError, LobbyJoinExternalInfo>

// Lobby See

sealed class LobbySeekError : LobbyServicesError {
    class UserAlreadyInALobby(val lobbyID: Int) : LobbySeekError()
}

typealias LobbySeekResult = Either<LobbySeekError, PublicLobbyExternalInfo>

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
    val name: String,
    val user1: PublicUserExternalInfo,
    val user2: PublicUserExternalInfo? = null,
    val gridSize: Int,
    val opening: String,
    val winningLength: Int,
    val pointsMargin: Int,
    val overflow: Boolean,
    val game: GameExternalInfo?

) {
    constructor(lobby: Lobby, game: GameExternalInfo? = null) : this(
        lobby.id,
        lobby.name,
        lobby.user1.toPublicExternalInfo(),
        lobby.user2?.toPublicExternalInfo(),
        lobby.settings.boardSize,
        lobby.settings.opening.toString(),
        lobby.settings.winningLength,
        lobby.pointsMargin,
        lobby.settings.overflowAllowed,
        game
    )

    fun isLobbyStarted() = game != null && user2 != null
}

sealed class LobbyDetailsError : LobbyServicesError {
    object LobbyNotFound : LobbyDetailsError()
}
typealias LobbyDetailsResult = Either<LobbyDetailsError, PublicLobbyExternalInfo>
