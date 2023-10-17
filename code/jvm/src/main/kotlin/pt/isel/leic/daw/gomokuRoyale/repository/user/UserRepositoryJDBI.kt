package pt.isel.leic.daw.gomokuRoyale.repository.user

import kotlinx.datetime.Instant
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import org.slf4j.LoggerFactory
import pt.isel.leic.daw.gomokuRoyale.domain.token.Token
import pt.isel.leic.daw.gomokuRoyale.domain.token.TokenValidationInfo
import pt.isel.leic.daw.gomokuRoyale.domain.user.User

class UserRepositoryJDBI(private val handle: Handle) : UserRepository {

    override fun createUser(username: String, email: String, password: String, rating: Double): Int =
        handle.createUpdate(
            """
            insert into users (username, email, password) values (:username, :email, :password)
            """
        )
            .bind("username", username)
            .bind("email", email)
            .bind("password", password)
            .executeAndReturnGeneratedKeys()
            .mapTo<Int>()
            .one()

    override fun getUserByEmail(email: String): User? =
        handle.createQuery("select * from users where email = :email")
            .bind("email", email)
            .mapTo<User>()
            .singleOrNull()

    override fun getAllUsers(): List<User> =
        handle.createQuery("select * from users")
            .mapTo<User>()
            .toList()

    override fun getUserByUsername(username: String): User? =
        handle.createQuery("select * from users where username = :username")
            .bind("username", username)
            .mapTo<User>()
            .singleOrNull()

    override fun isUserStoredByUsername(username: String): Boolean =
        handle.createQuery("select exists (select 1 from users where username = :username)")
            .bind("username", username)
            .mapTo(Boolean::class.java)
            .first()

    override fun isUserStoredByEmail(email: String): Boolean =
        handle.createQuery("select exists (select 1 from users where email = :email)")
            .bind("email", email)
            .mapTo(Boolean::class.java)
            .first()

    override fun createToken(userId: Int, token: Token, maxTokens: Int): Int {
        val deletions = handle.createUpdate(
            """
            delete from tokens where user_id = :user_id
            """
        )
            .bind("user_id", userId)
            .execute()

        logger.info("{} tokens deleted when creating new token", deletions)

        return handle.createUpdate(
            """
            insert into tokens (token, user_id, created_at, last_used_at) values (:token, :user_id, :created_at, :last_used_at)
            """
        )
            .bind("token", token.token)
            .bind("user_id", userId)
            .bind("created_at", token.createdAt.epochSeconds)
            .bind("last_used_at", token.lastUsedAt.epochSeconds)
            .execute()
    }

    override fun updateTokenLastUsedAt(token: Token, now: Instant) {
        handle.createUpdate(
            """
                update tokens
                set last_used_at = :last_used_at
                where token = :token
            """.trimIndent()
        )
            .bind("last_used_at", now.epochSeconds)
            .bind("token", token.token)
            .execute()
    }

    override fun getTokenByTokenValidationInfo(tokenValidationInfo: TokenValidationInfo): Pair<User, Token>? =
        handle.createQuery(
            """
                select u.id as id, username, email, password, token, created_at as createdAt, last_used_at as lastUsedAt
                from users as u
                inner join tokens as t
                on u.id = t.user_id
                where token = :validation_information
            """
        )
            .bind("validation_information", tokenValidationInfo.validationInfo)
            .mapTo<UserAndTokenModel>()
            .singleOrNull()
            ?.userAndToken

    override fun removeTokenByValidationInfo(tokenValidationInfo: TokenValidationInfo): Int {
        return handle.createUpdate(
            """
                delete from tokens
                where token = :validation_information
            """
        )
            .bind("validation_information", tokenValidationInfo.validationInfo)
            .execute()
    }

    override fun getUserByTokenValidationInfo(tokenValidationInfo: TokenValidationInfo): User? {
        return handle.createQuery(
            """
                select u.id, username, email,  password
                from users as u
                inner join tokens as t
                on u.id = t.user_id
                where token = :validation_information
            """
        )
            .bind("validation_information", tokenValidationInfo.validationInfo)
            .mapTo<User>()
            .singleOrNull()
    }

    private data class UserAndTokenModel(
        val id: Int,
        val username: String,
        val email: String,
        val password: String,
        val token: TokenValidationInfo,
        val createdAt: Long,
        val lastUsedAt: Long
    ) {
        val userAndToken: Pair<User, Token>
            get() = Pair(
                User(id, username, email, password),
                Token(
                    Instant.fromEpochSeconds(createdAt),
                    Instant.fromEpochSeconds(lastUsedAt),
                    id,
                    token.validationInfo
                )
            )
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UserRepositoryJDBI::class.java)
    }
}
