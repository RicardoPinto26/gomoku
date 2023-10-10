package pt.isel.leic.daw.gomokuRoyale.services.users

import kotlinx.datetime.Clock
import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.domain.token.Token
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.domain.user.UserDomain
import pt.isel.leic.daw.gomokuRoyale.repository.TransactionManager
import pt.isel.leic.daw.gomokuRoyale.utils.Either
import pt.isel.leic.daw.gomokuRoyale.utils.failure
import pt.isel.leic.daw.gomokuRoyale.utils.success


@Component
class UsersServiceImpl(
    private val transactionManager: TransactionManager,
    private val userDomain: UserDomain,
    private val clock: Clock
) : UsersService {

    override fun registerUser(username: String, email: String, password: String): UserCreationResult {
        userDomain.checkUserCredentialsRegister(username, email, password)
        val hashedPassword = userDomain.hashPassword(password)

        return transactionManager.run {
            val userRepo = it.usersRepository
            if (userRepo.isUserStoredByUsername(username)) {
                failure(UserCreationError.UserAlreadyExists)
            }
            if (userRepo.isUserStoredByEmail(email)) {
                failure(UserCreationError.UserAlreadyExists)
            } else {
                val id = userRepo.createUser(username, email, hashedPassword)
                success(id)
            }
        }
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

        return success(UserExternalInfo(user.username, user.gamesPlayed, user.rating.toInt()))
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
