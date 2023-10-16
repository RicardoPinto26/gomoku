package pt.isel.leic.daw.gomokuRoyale.services.users

import kotlinx.datetime.Clock
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.domain.token.Token
import pt.isel.leic.daw.gomokuRoyale.domain.token.TokenValidationInfo
import pt.isel.leic.daw.gomokuRoyale.domain.user.STARTING_RATING
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.domain.user.UserDomain
import pt.isel.leic.daw.gomokuRoyale.repository.TransactionManager
import pt.isel.leic.daw.gomokuRoyale.utils.Either
import pt.isel.leic.daw.gomokuRoyale.utils.failure
import pt.isel.leic.daw.gomokuRoyale.utils.success


@Component
class UsersServiceImpl(
    //TODO: CHANGE FROM TransactionManager TO SOMETHING THAT ENABLES MOCKING (UsersRepository)
    private val transactionManager: TransactionManager,
    private val userDomain: UserDomain,
    private val clock: Clock
) : UsersService {

    override fun registerUser(username: String, email: String, password: String): UserCreationResult {
        logger.info("Starting registration of user {}", username)

        logger.info("Checking user credentials")
        try {
            userDomain.checkUserCredentialsRegister(username, email, password)
        } catch (e: Exception) {
            return failure(UserCreationError.InsecurePassword)
        }
        logger.info("Checked user credentials")

        logger.info("Hashing password")
        val hashedPassword = userDomain.hashPassword(password)
        logger.info("Hashed password")

        logger.info("Starting Transaction")
        return transactionManager.run {
            val userRepo = it.usersRepository
            logger.info("Checking if user exists via username")
            if (userRepo.isUserStoredByUsername(username)) {
                return@run failure(UserCreationError.UserAlreadyExists)
            }
            logger.info("Checking if user exists via email")
            if (userRepo.isUserStoredByEmail(email)) {
                return@run failure(UserCreationError.UserAlreadyExists)
            }
            logger.info("creating user")
            return@run try {
                userRepo.createUser(username, email, hashedPassword, STARTING_RATING)
                success(
                    UserExternalInfo(
                        username = username,
                        email = email,
                        rating = STARTING_RATING.toInt(),
                        gamesPlayed = 0
                    )
                )
            } catch(e:Exception) {
                failure(UserCreationError.UserAlreadyExists)
            }
        }
    }

    override fun createToken(username: String, password: String): TokenCreationResult {
        userDomain.checkUserCredentialsLogin(username, null, password)

        return transactionManager.run {
            val userRepo = it.usersRepository
            val user: User = userRepo.getUserByUsername(username)
                ?: return@run failure(TokenCreationError.UserOrPasswordAreInvalid)
            if (!userDomain.checkPassword(password, user.password)) {
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

        return success(PublicUserExternalInfo(user.username, user.nr_games_played, user.rating.toInt()))
    }

    override fun getUserByToken(token: String): User? {
        logger.info("Trying to get user by token")
        if (!userDomain.canBeToken(token)) {
            logger.info("returning null when trying to ger user by token")
            return null
        }

        return transactionManager.run {
            val usersRepository = it.usersRepository
            logger.info("userRepo : $usersRepository")
            val tokenValidationInfo = TokenValidationInfo(token)
            logger.info("tokenValidInfo : $tokenValidationInfo")
            val userAndToken = usersRepository.getTokenByTokenValidationInfo(tokenValidationInfo)
            logger.info("userAndToken : $userAndToken")
            if (userAndToken != null && userDomain.isTokenTimeValid(clock, userAndToken.second)) {
                usersRepository.updateTokenLastUsedAt(userAndToken.second, clock.now())
                userAndToken.first
            } else {
                logger.info("returning null when trying to ger user by token with transaction manager")
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

    companion object {
        private val logger = LoggerFactory.getLogger(UsersServiceImpl::class.java)
    }
}
