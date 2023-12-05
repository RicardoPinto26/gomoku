package pt.isel.leic.daw.gomokuRoyale.services.users

import kotlinx.datetime.Clock
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserInvalidEmail
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserInvalidPassword
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserInvalidUsername
import pt.isel.leic.daw.gomokuRoyale.domain.token.Token
import pt.isel.leic.daw.gomokuRoyale.domain.token.TokenValidationInfo
import pt.isel.leic.daw.gomokuRoyale.domain.user.STARTING_RATING
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.domain.user.UserDomain
import pt.isel.leic.daw.gomokuRoyale.repository.TransactionManager
import pt.isel.leic.daw.gomokuRoyale.utils.Either
import pt.isel.leic.daw.gomokuRoyale.utils.Success
import pt.isel.leic.daw.gomokuRoyale.utils.failure
import pt.isel.leic.daw.gomokuRoyale.utils.success

@Component
class UserServiceImpl(
    private val transactionManager: TransactionManager,
    private val userDomain: UserDomain,
    private val clock: Clock
) : UserService {

    override fun registerUser(username: String, email: String, password: String): UserCreationResult {
        logger.info("Starting registration of user {}", username)

        logger.info("Checking user credentials")
        try {
            userDomain.checkUserCredentialsRegister(username, email, password)
        } catch (e: Exception) {
            when (e) {
                is UserInvalidUsername -> return failure(UserCreationError.InvalidUsername)
                is UserInvalidEmail -> return failure(UserCreationError.InvalidEmail)
                is UserInvalidPassword -> return failure(UserCreationError.InsecurePassword)
            }
        }
        logger.info("Checked user credentials")

        logger.info("Hashing password")
        val hashedPassword = userDomain.hashPassword(password)
        logger.info("Hashed password")

        logger.info("Starting Transaction")
        return transactionManager.run {
            val userRepo = it.userRepository
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
            } catch (e: Exception) {
                failure(UserCreationError.UserAlreadyExists)
            }
        }
    }

    override fun createToken(username: String, password: String): TokenCreationResult {
        userDomain.checkUserCredentialsLogin(username, null, password)

        return transactionManager.run {
            val userRepo = it.userRepository
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
            val userRepo = it.userRepository
            userRepo.getUserByUsername(username)
        } ?: return failure(GetUserStatsError.NoSuchUser)

        return success(PublicUserExternalInfo(user.username, user.nrGamesPlayed, user.rating.toInt()))
    }

    override fun getUserByToken(token: String): User? {
        logger.info("Trying to get user by token")
        if (!userDomain.canBeToken(token)) {
            logger.info("returning null when trying to get user by token")
            return null
        }

        return transactionManager.run {
            val usersRepository = it.userRepository
            val tokenValidationInfo = TokenValidationInfo(token)
            val userAndToken = usersRepository.getTokenByTokenValidationInfo(tokenValidationInfo)
            logger.info("userAndToken : $userAndToken")
            if (userAndToken != null && userDomain.isTokenTimeValid(clock, userAndToken.second)) {
                usersRepository.updateTokenLastUsedAt(userAndToken.second, clock.now())
                userAndToken.first
            } else {
                logger.info("returning null when trying to get user by token with transaction manager")
                null
            }
        }
    }

    override fun getUserByID(id: Int): User? {
        logger.info("Trying to get user by id")
        if (id < 0) {
            logger.info("returning null id below 0")
            return null
        }

        return transactionManager.run {
            val usersRepository = it.userRepository
            logger.info("userRepo : $usersRepository")
            usersRepository.getUserByID(id)
        }
    }

    override fun revokeToken(token: String): Boolean {
        val tokenValidationInfo = TokenValidationInfo(token)
        return transactionManager.run {
            it.userRepository.removeTokenByValidationInfo(tokenValidationInfo) == 1
        }
    }

    override fun getUsersRanking(skip: Int, limit: Int): GetUsersRankingResult {
        return transactionManager.run {
            val userRepo = it.userRepository
            val users: List<User> = userRepo.getAllUsers(skip, limit)

            return@run Success(
                GetUsersRankingExternalInfo(
                    users.sortedByDescending { user -> user.rating }.map { user ->
                        PublicUserExternalInfo(
                            username = user.username,
                            gamesPlayed = user.nrGamesPlayed,
                            rating = user.rating.toInt()
                        )
                    }
                )
            )
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UserServiceImpl::class.java)
    }
}
