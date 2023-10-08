package pt.isel.leic.daw.gomokuRoyale.domain.user

import java.util.*

/**
 * Token entity
 * @property createdAt date of the token creation
 * @property lastUsedAt date of the token last usage
 * @property userID user's unique identifier
 * @property token the token
 */
data class Token(
    val createdAt: Date,
    val lastUsedAt: Date,
    val userID: Int,
    val token: String
) {
    companion object {

        /**
         * Checks occurs finish date occurs after initial date
         *
         * @param createDate date of the token creation
         * @param lastUsedDate date of the token last usage
         *
         * @return true if the name is valid, false if not
         */
        fun validDates(createDate: Date, lastUsedDate: Date) = lastUsedDate > createDate
    }
    init {
        require(
            validDates(
                createdAt,
                lastUsedAt
            )
        ) { "The token last used date must be more recent than the creation date" }
    }
}
