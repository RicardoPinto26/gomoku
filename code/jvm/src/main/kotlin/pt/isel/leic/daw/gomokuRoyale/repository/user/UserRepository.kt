package pt.isel.leic.daw.gomokuRoyale.repository.user

import kotlinx.datetime.Instant
import pt.isel.leic.daw.gomokuRoyale.domain.token.Token
import pt.isel.leic.daw.gomokuRoyale.domain.token.TokenValidationInfo
import pt.isel.leic.daw.gomokuRoyale.domain.user.User

interface UserRepository {
    fun createUser(username: String, email: String, password: String, rating: Double): Int

    fun isUserStoredByUsername(username: String): Boolean

    fun isUserStoredByEmail(email: String): Boolean

    fun getUserByID(id: Int): User?

    fun getUserByUsername(username: String): User?

    fun getUserByEmail(email: String): User?

    fun getAllUsers(skip: Int, limit: Int): List<User>

    fun createToken(userId: Int, token: Token, maxTokens: Int): Int

    fun updateTokenLastUsedAt(token: Token, now: Instant)

    fun getTokenByTokenValidationInfo(tokenValidationInfo: TokenValidationInfo): Pair<User, Token>?

    fun removeTokenByValidationInfo(tokenValidationInfo: TokenValidationInfo): Int

    fun getUserByTokenValidationInfo(tokenValidationInfo: TokenValidationInfo): User?

    /**
     * Changes user rating
     * @param id id of the user
     * @param newRating new rating of the user
     *
     * @return true if the rating was changed, false if not
     */

    fun changeUserRating(id: Int, newRating: Int): Boolean

    fun increaseGamesPlayed(id: Int, value: Int): Boolean
}
