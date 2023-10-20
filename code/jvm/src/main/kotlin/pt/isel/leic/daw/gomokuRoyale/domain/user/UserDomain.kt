package pt.isel.leic.daw.gomokuRoyale.domain.user

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserInvalidEmail
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserInvalidPassword
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserInvalidUsername
import pt.isel.leic.daw.gomokuRoyale.domain.token.Token
import pt.isel.leic.daw.gomokuRoyale.domain.token.TokenEncoder
import pt.isel.leic.daw.gomokuRoyale.domain.token.TokenValidationInfo
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

/**
 * User domain entity.
 * Contains information about how a user's token is encoded, it's size, ttl information and max amount of tokens the user can have
 *
 * @property tokenEncoder the token encoder
 * @property config contains token information and max amount of token a user can have
 */
@Component
class UserDomain(
    val tokenEncoder: TokenEncoder,
    val config: UserDomainConfig
) {
    companion object {
        private val logger = LoggerFactory.getLogger(UserDomain::class.java)
    }

    /**
     * Converts a byte array to hex string
     *
     * @param bytes byte array to be encoded
     *
     * @return string with hexadecimal values
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = "0123456789ABCDEF"
        val hex = StringBuilder(2 * bytes.size)
        for (i in bytes.indices) {
            val b = bytes[i].toInt() and 0xFF
            hex.append(hexChars[b.ushr(4)])
            hex.append(hexChars[b and 0x0F])
        }
        return hex.toString()
    }

    /**
     * Returns hashed password
     *
     * @param password the user's password
     *
     * @return string with hashed password
     */
    fun hashPassword(password: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = messageDigest.digest(password.toByteArray(StandardCharsets.UTF_8))
        return bytesToHex(hashedBytes)
    }

    /**
     * Returns hashed password
     *
     * @param password the user's password
     *
     * @return string with hashed password
     */
    fun checkPassword(password: String, hashedPassword: String): Boolean {
        return hashPassword(password) == hashedPassword
    }

    /**
     * Checks if user's credentials upon registration are valid
     *
     * @param name the user's name
     * @param email the user's email
     * @param password the user's password
     *
     * @throws IllegalArgumentException if any of the parameters are invalid
     */
    fun checkUserCredentialsRegister(name: String, email: String, password: String) {
        if (!User.validName(name)) UserInvalidUsername("Invalid username: $name")
        if (!User.validEmail(email)) UserInvalidEmail("Invalid email: $email")
        if (!User.isSafePassword(password)) UserInvalidPassword("Insecure password")
    }

    /**
     * Checks if user's credentials upon login are valid
     *
     * @param name the user's name
     * @param email the user's email
     * @param password the user's password
     *
     * @throws IllegalArgumentException if any of the parameters are invalid
     */
    fun checkUserCredentialsLogin(name: String?, email: String?, password: String) {
        require(name != null || email != null) { "Invalid credentials" }
        require(password.isNotEmpty()) { "Invalid credentials" }
    }

    /**
     * Creates a token according to [config] information
     *
     * @returns string with token
     */
    fun generateTokenValue(): String =
        ByteArray(config.tokenSizeInBytes).let { byteArray ->
            SecureRandom.getInstanceStrong().nextBytes(byteArray)
            Base64.getUrlEncoder().encodeToString(byteArray)
        }

    fun canBeToken(token: String): Boolean = try {
        Base64.getUrlDecoder()
            .decode(token).size == config.tokenSizeInBytes
    } catch (ex: IllegalArgumentException) {
        false
    }

    fun getTokenExpiration(token: Token): Instant {
        val absoluteExpiration = token.createdAt + config.tokenTtl
        val rollingExpiration = token.lastUsedAt + config.tokenRollingTtl
        return if (absoluteExpiration < rollingExpiration) {
            absoluteExpiration
        } else {
            rollingExpiration
        }
    }

    fun isTokenTimeValid(
        clock: Clock,
        token: Token
    ): Boolean {
        val now = clock.now()
        logger.info("token.createdAt:${token.createdAt}, $now")
        logger.info("now-token.createdAt: ${now - token.createdAt}, ${config.tokenTtl}")
        logger.info("now-token.lastUsedAt: ${now - token.lastUsedAt}, ${config.tokenRollingTtl}")

        return token.createdAt <= now &&
            (now - token.createdAt) <= config.tokenTtl
        // && (now - token.lastUsedAt) <= config.tokenRollingTtl
    }

    fun createTokenValidationInformation(token: String): TokenValidationInfo =
        tokenEncoder.createValidationInformation(token)
}
