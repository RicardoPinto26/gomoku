package pt.isel.leic.daw.gomokuRoyale.domain.user

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import kotlin.math.pow

const val RATING_FACTOR = 32
const val C = 400

/**
 * User entity
 *
 * @property id user's unique identifier
 * @property username name of the user
 * @property email email of the user
 * @property gamesPlayed number os games the user as played
 * @property rating the user's rating
 * @property hashPassword the users password (encoded to sha265)
 */
data class User(
    private val id: Int,
    private val username: String,
    private val email: String,
    private val hashPassword: String,
    private val gamesPlayed: Int = 0,
    val rating: Double = 800.0
) {
    companion object {
        private const val EMAIL_REGEX = "^(.+)@(.+)$"
        const val MAX_NAME_LENGTH = 50
        const val MIN_NAME_LENGTH = 3

        /**
         * Checks whether an email is valid or not
         *
         * @param email String with email to check
         *
         * @return true if valid, false if not
         */
        fun validEmail(email: String): Boolean = email.matches(EMAIL_REGEX.toRegex())

        /**
         * Checks whether a username is valid or not
         *
         * @param name String with name of the user
         *
         * @return true if the name is valid, false if not
         */
        fun validName(name: String): Boolean = (name.length in MIN_NAME_LENGTH..MAX_NAME_LENGTH)

        /**
         * Checks whether an id is valid or not
         *
         * @param id Int with id to check
         *
         * @return true if valid, false if not
         */
        fun validId(id: Int): Boolean = id >= 0

        /**
         * Checks whether a password is safe or not
         *
         * @param password String with the password
         *
         * @return true if the password includes a digit, lowercase and uppercase
         */

        fun isSafePassword(password: String) =
            password.length >= 8 && password.any { it.isDigit() } && password.any { it.isUpperCase() } && password.any { it.isLowerCase() }
    }

    init {
        require(validName(username)) { "Invalid username: $username" }
        require(validEmail(email)) { "Invalid email: $email" }
        require(validId(id)) { "Invalid user id: $id" }
    }

    fun bytesToHex(bytes: ByteArray): String {
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

    fun checkUserCredentials(name: String, email: String, password: String) {
        require(validName(name)) { "Invalid username: $name" }
        require(validEmail(email)) { "Invalid email: $email" }
        require(isSafePassword(password)) { "Insecure password" }
    }

    fun calculateNewRating(result: Double, opponentRating: Double): Double {
        val qa = 10.0.pow(rating/C)
        val qb = 10.0.pow(opponentRating/C)
        val expectedScore = qa / (qa + qb)
        return rating + RATING_FACTOR * (result - expectedScore)
    }
}
