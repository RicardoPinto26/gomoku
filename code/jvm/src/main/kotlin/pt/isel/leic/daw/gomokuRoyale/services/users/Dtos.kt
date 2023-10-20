package pt.isel.leic.daw.gomokuRoyale.services.users

import pt.isel.leic.daw.gomokuRoyale.services.ServicesError
import pt.isel.leic.daw.gomokuRoyale.utils.Either

sealed interface UserServicesError : ServicesError

sealed class UserCreationError : UserServicesError {
    object UserAlreadyExists : UserCreationError()
    object InsecurePassword : UserCreationError()
    object InvalidUsername : UserCreationError()
    object InvalidEmail : UserCreationError()
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

sealed class TokenCreationError : UserServicesError {
    object UserOrPasswordAreInvalid : TokenCreationError()
}

typealias TokenCreationResult = Either<TokenCreationError, TokenExternalInfo>

data class PublicUserExternalInfo(
    val username: String,
    val gamesPlayed: Int,
    val rating: Int
)

sealed class GetUserStatsError : UserServicesError {
    object NoSuchUser : GetUserStatsError()
}

typealias GetUserStatsResult = Either<GetUserStatsError, PublicUserExternalInfo>
