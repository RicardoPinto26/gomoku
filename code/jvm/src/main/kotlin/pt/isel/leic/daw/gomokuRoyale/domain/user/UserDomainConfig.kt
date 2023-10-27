package pt.isel.leic.daw.gomokuRoyale.domain.user

import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.TokenRollingTTLNegative
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.TokenSizeZeroOrNegative
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.TokenTTLNegative
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserCantHaveTokens
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
        if (tokenSizeInBytes <= 0) throw TokenSizeZeroOrNegative("Token size must be bigger than 0, is $tokenSizeInBytes")
        if (tokenTtl.isNegative()) throw TokenTTLNegative("Token time to live must be positive, is $tokenTtl")
        if (tokenRollingTtl.isNegative()) throw TokenRollingTTLNegative("Token tolling time to live must be  $tokenRollingTtl")
        if (maxTokensPerUser <= 0) throw UserCantHaveTokens("User must have tokens, can have $maxTokensPerUser")
    }
}
