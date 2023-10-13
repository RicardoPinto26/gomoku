package pt.isel.leic.daw.gomokuRoyale.repository.user

import kotlinx.datetime.Instant
import pt.isel.leic.daw.gomokuRoyale.domain.token.Token
import pt.isel.leic.daw.gomokuRoyale.domain.token.TokenValidationInfo
import pt.isel.leic.daw.gomokuRoyale.domain.user.User

interface UsersRepository {
    fun createUser(username: String, email: String, password: String, rating: Double): Int

    fun isUserStoredByUsername(username: String): Boolean

    fun isUserStoredByEmail(email: String): Boolean

    fun getUserByUsername(username: String): User?

    fun getUserByEmail(email: String): User?

    fun getAllUsers(): List<User>

    fun createToken(userId: Int, token: Token, maxTokens: Int): Int

    fun updateTokenLastUsedAt(token: Token, now: Instant)

    fun getTokenByTokenValidationInfo(tokenValidationInfo: TokenValidationInfo): Pair<User, Token>?

    fun removeTokenByValidationInfo(tokenValidationInfo: TokenValidationInfo): Int

    /*
          fun loginUserByUsername(username: String, password: String): User

          fun loginUserByEmail(email: String, password: String): User
     */
}
