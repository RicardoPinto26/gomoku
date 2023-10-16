package pt.isel.leic.daw.gomokuRoyale.http.pipeline

import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.domain.AuthenticatedUser
import pt.isel.leic.daw.gomokuRoyale.services.users.UsersService

@Component
class RequestTokenProcessor(
    val usersService: UsersService
) {


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
        return usersService.getUserByToken(parts[1])?.let {
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
