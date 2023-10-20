package pt.isel.leic.daw.gomokuRoyale.domain.user

import kotlin.time.Duration

/**
 * Contains tokens information and max amount of tokens per user
 *
 * @property tokenSizeInBytes number of bytes that make up a token
 * @property tokenTtl duration  until token dies
 * @property tokenRollingTtl number of bytes that make up a token
 * @property maxTokensPerUser number of bytes that make up a token
 *
 */
data class UserDomainConfig(
    val tokenSizeInBytes: Int,
    val tokenTtl: Duration,
    val tokenRollingTtl: Duration,
    val maxTokensPerUser: Int
) {
    init {
        require(tokenSizeInBytes > 0)
        require(tokenTtl.isPositive())
        require(tokenRollingTtl.isPositive())
        require(maxTokensPerUser > 0)
    }
}
