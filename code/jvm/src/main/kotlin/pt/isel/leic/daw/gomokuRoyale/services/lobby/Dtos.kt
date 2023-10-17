package pt.isel.leic.daw.gomokuRoyale.services.lobby

import pt.isel.leic.daw.gomokuRoyale.domain.Game
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.utils.Either

sealed class LobbyCreationError {
    object UserNotFound : LobbyCreationError()
}

data class LobbyExternalInfo(
    val id: Int, // lobbyId
    val game: Game? = null,
    val user1: User,
    val user2: User? = null,
    val gridSize: Int,
    val opening: String,
    val variant: String,
    val pointsMargin: Int
)

typealias LobbyCreationResult = Either<LobbyCreationError, LobbyExternalInfo>

sealed class LobbyJoinError {
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

typealias LobbyJoinResult = Either<LobbyJoinError, LobbyJoinExternalInfo>
