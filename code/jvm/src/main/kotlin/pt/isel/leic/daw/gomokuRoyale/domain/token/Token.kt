package pt.isel.leic.daw.gomokuRoyale.domain.token

import kotlinx.datetime.Instant

/**
 * Token entity
 * @property createdAt date of the token creation
 * @property lastUsedAt date of the token last usage
 * @property userID user's unique identifier
 * @property token the token
 */
data class Token(
    val createdAt: Instant,
    val lastUsedAt: Instant,
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
        fun validDates(createDate: Instant, lastUsedDate: Instant) = lastUsedDate > createDate
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
