package pt.isel.leic.daw.gomokuRoyale.domain.user

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.domain.token.Token
import pt.isel.leic.daw.gomokuRoyale.domain.token.TokenEncoder
import pt.isel.leic.daw.gomokuRoyale.domain.token.TokenValidationInfo
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

@Component
class UserDomain(
    val tokenEncoder: TokenEncoder,
    val config: UserDomainConfig
) {

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

    fun hashPassword(password: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = messageDigest.digest(password.toByteArray(StandardCharsets.UTF_8))
        return bytesToHex(hashedBytes)
    }

    fun checkPassword(password: String, hashedPassword: String): Boolean {
        return hashPassword(password) == hashedPassword
    }

    fun checkUserCredentialsRegister(name: String, email: String, password: String) {
        require(User.validName(name)) { "Invalid username: $name" }
        require(User.validEmail(email)) { "Invalid email: $email" }
        require(User.isSafePassword(password)) { "Insecure password" }
    }

    fun checkUserCredentialsLogin(name: String?, email: String?, password: String) {
        require(name != null || email != null) { "Invalid credentials" }
        require(password.isNotEmpty()) { "Invalid credentials" }
    }

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
        return token.createdAt <= now &&
            (now - token.createdAt) <= config.tokenTtl &&
            (now - token.lastUsedAt) <= config.tokenRollingTtl
    }

    fun createTokenValidationInformation(token: String): TokenValidationInfo =
        tokenEncoder.createValidationInformation(token)
}
