package pt.isel.leic.daw.gomokuRoyale.domain.token

import org.springframework.stereotype.Component

@Component
interface TokenEncoder {
    fun createValidationInformation(token: String): TokenValidationInfo
}