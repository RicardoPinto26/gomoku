package pt.isel.leic.daw.gomokuRoyale.http.pipeline

import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.domain.AuthenticatedUser
import pt.isel.leic.daw.gomokuRoyale.services.users.UserService

/**
 * Component for processing Authorization header values to authenticate and retrieve user information.
 *
 * @property userService - The [UserService] instance used for retrieving user information.
 */
@Component
class RequestTokenProcessor(
    val userService: UserService
) {
    /**
     * Processes the Authorization header value to authenticate and retrieve user information.
     *
     * @param authorizationValue - The Authorization header value containing the bearer token.
     * @return An [AuthenticatedUser] object if authentication is successful, or null otherwise.
     */
    fun processAuthorizationHeaderValue(authorizationValue: String?): AuthenticatedUser? {
        if (authorizationValue == null) {
            return null
        }
        val parts = authorizationValue.trim().split(" ")

        if (parts.size != 2) {
            return null
        }
        if (parts[0].lowercase() != SCHEME) {
            return null
        }
        return userService.getUserByToken(parts[1])?.let {
            AuthenticatedUser(
                it,
                parts[1]
            )
        }
    }

    companion object {
        const val SCHEME = "bearer"
    }
}
