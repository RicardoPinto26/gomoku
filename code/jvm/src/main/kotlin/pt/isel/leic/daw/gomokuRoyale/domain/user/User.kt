package pt.isel.leic.daw.gomokuRoyale.domain.user

import kotlin.math.pow

const val STARTING_RATING = 800.0
const val RATING_FACTOR = 32
const val C = 400

/**
 * User entity
 *
 * @property id user's unique identifier
 * @property username name of the user
 * @property email email of the user
 * @property nr_games_played number os games the user as played
 * @property rating the user's rating
 * @property password the users password (encoded to sha265)
 */
data class User(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val rating: Double = STARTING_RATING,
    val nr_games_played: Int = 0
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


    fun calculateNewRating(result: Double, opponentRating: Double): Double {
        val qa = 10.0.pow(rating / C)
        val qb = 10.0.pow(opponentRating / C)
        val expectedScore = qa / (qa + qb)
        return rating + RATING_FACTOR * (result - expectedScore)
    }
}
