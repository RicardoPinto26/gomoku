package pt.isel.leic.daw.gomokuRoyale.services.users

import pt.isel.leic.daw.gomokuRoyale.utils.Either

sealed class UserCreationError {
    object UserAlreadyExists : UserCreationError()
    object InsecurePassword : UserCreationError()
}

data class UserExternalInfo(
    val username: String,
    val email: String,
    val gamesPlayed: Int,
    val rating: Int
)

typealias UserCreationResult = Either<UserCreationError, UserExternalInfo>

data class TokenExternalInfo(
    val tokenValue: String,
    val tokenExpiration: kotlinx.datetime.Instant
)

sealed class TokenCreationError {
    object UserOrPasswordAreInvalid : TokenCreationError()
}

typealias TokenCreationResult = Either<TokenCreationError, TokenExternalInfo>

data class PublicUserExternalInfo(
    val username: String,
    val gamesPlayed: Int,
    val rating: Int
)

sealed class GetUserStatsError {
    object NoSuchUser : GetUserStatsError()
}

typealias GetUserStatsResult = Either<GetUserStatsError, PublicUserExternalInfo>