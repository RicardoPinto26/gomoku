package pt.isel.leic.daw.gomokuRoyale.services.user

import kotlinx.datetime.Clock
import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.domain.token.Token
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.domain.user.UserDomain
import pt.isel.leic.daw.gomokuRoyale.repository.jdbi.JdbiTransactionManager
import pt.isel.leic.daw.gomokuRoyale.services.dtos.user.UserStats
import pt.isel.leic.daw.gomokuRoyale.services.exceptions.UserInvalidCredentialsException
import pt.isel.leic.daw.gomokuRoyale.utils.Either
import pt.isel.leic.daw.gomokuRoyale.utils.failure
import pt.isel.leic.daw.gomokuRoyale.utils.success

data class TokenExternalInfo(
    val tokenValue: String,
    val tokenExpiration: kotlinx.datetime.Instant
)

sealed class UserCreationError {
    object UserAlreadyExists : UserCreationError()
    object InsecurePassword : UserCreationError()
}
typealias UserCreationResult = Either<UserCreationError, Int>

sealed class TokenCreationError {
    object UserOrPasswordAreInvalid : TokenCreationError()
}

sealed class GetUserStatsError {
    object NoSuchUser : GetUserStatsError()
}

typealias TokenCreationResult = Either<TokenCreationError, TokenExternalInfo>

typealias GetUserStatsResult = Either<GetUserStatsError, UserStats>

@Component
class UsersService(
    private val transactionManager: JdbiTransactionManager,
    private val userDomain: UserDomain,
    private val clock: Clock
) : UserServiceInterface {

    override fun registerUser(user : RegisterInputDTO): RegisterOutputDTO {
        userDomain.checkUserCredentialsRegister(user.username, user.email, user.password)
        val hashedPassword = userDomain.hashPassword(user.password)

        return transactionManager.run {
            val userRepo = it.usersRepository
            if (userRepo.isUserStoredByUsername(user.username)) {
                throw UserAlreadyExistsException("User with username ${user.username} already exists")
            }
            if(userRepo.isUserStoredByEmail(user.email)) {
                throw UserAlreadyExistsException("User with email ${user.email} already exists")
            } else {
                userRepo.createUser(user.username, user.email, hashedPassword)
            }
        }
        return RegisterOutputDTO(id, user.username)
    }

    override fun loginUser(username: String?, email: String?, password: String): User {
        userDomain.checkUserCredentialsLogin(username, email, password)

        val user = transactionManager.run {
            val userRepo = it.usersRepository
            if (username != null) {
                userRepo.getUserByUsername(username)
            } else {
                userRepo.getUserByEmail(email!!)
            }
        }
        if (user == null || !userDomain.checkPassword(password, user.hashPassword)) {
            throw UserInvalidCredentialsException("Invalid credentials")
        }
        return user //create dto
    }

    override fun createToken(username: String, password: String): TokenCreationResult {
        userDomain.checkUserCredentialsLogin(username, null, password)

        return transactionManager.run {
            val userRepo = it.usersRepository
            val user: User = userRepo.getUserByUsername(username)
                ?: return@run failure(TokenCreationError.UserOrPasswordAreInvalid)
            if (!userDomain.checkPassword(password, user.hashPassword)) {
                return@run failure(TokenCreationError.UserOrPasswordAreInvalid)
            }

            val tokenValue = userDomain.generateTokenValue()
            val now = clock.now()
            val newToken = Token(
                createdAt = now,
                lastUsedAt = now,
                userID = user.id,
                token = tokenValue
            )
            userRepo.createToken(user.id, newToken, userDomain.config.maxTokensPerUser)
            Either.Right(
                TokenExternalInfo(
                    tokenValue,
                    userDomain.getTokenExpiration(newToken)
                )
            )
        }
    }

    override fun getStats(username: String): GetUserStatsResult {
        if (username.isBlank()) {
            throw IllegalArgumentException()
        }

        val user = transactionManager.run {
            val userRepo = it.usersRepository
            userRepo.getUserByUsername(username)
        } ?: return failure(GetUserStatsError.NoSuchUser)

        return success(UserStats(user.gamesPlayed, user.rating.toInt()))
    }

    override fun getUserByToken(token: String): User? {
        if (!userDomain.canBeToken(token)) {
            return null
        }

        return transactionManager.run {
            val usersRepository = it.usersRepository
            val tokenValidationInfo = userDomain.createTokenValidationInformation(token)
            val userAndToken = usersRepository.getTokenByTokenValidationInfo(tokenValidationInfo)
            if (userAndToken != null && userDomain.isTokenTimeValid(clock, userAndToken.second)) {
                usersRepository.updateTokenLastUsedAt(userAndToken.second, clock.now())
                userAndToken.first
            } else {
                null
            }
        }
    }

    override fun revokeToken(token: String): Boolean {
        val tokenValidationInfo = userDomain.createTokenValidationInformation(token)
        return transactionManager.run {
            it.usersRepository.removeTokenByValidationInfo(tokenValidationInfo)
            true
        }
    }
}
