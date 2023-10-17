package pt.isel.leic.daw.gomokuRoyale.domain.user

import kotlin.time.Duration

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
