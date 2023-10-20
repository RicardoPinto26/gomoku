package pt.isel.leic.daw.gomokuRoyale.domain.token

import org.springframework.stereotype.Component

/**
 * The `TokenEncoder` interface defines a contract for components responsible for token encoding and validation
 */
@Component
interface TokenEncoder {

    /**
     * Creates a [TokenValidationInfo] instance from a given string
     *
     * @param token string with token
     *
     * @return [TokenValidationInfo] with hashed token
     */
    fun createValidationInformation(token: String): TokenValidationInfo
}
