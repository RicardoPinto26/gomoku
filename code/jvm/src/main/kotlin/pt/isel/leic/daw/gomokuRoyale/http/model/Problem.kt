package pt.isel.leic.daw.gomokuRoyale.http.model

import org.springframework.http.ResponseEntity
import java.net.URI

class Problem(
    typeUri: URI
) {
    val type = typeUri.toASCIIString()

    companion object {
        const val MEDIA_TYPE = "application/problem+json"
        fun response(status: Int, problem: Problem) = ResponseEntity
            .status(status)
            .header("Content-Type", MEDIA_TYPE)
            .body<Any>(problem)

        val userAlreadyExists = Problem(
            URI(
                "userAlreadyExists.com"
            )
        )
        val insecurePassword = Problem(
            URI(
                "insecurePassword.com"
            )
        )

        val userOrPasswordAreInvalid = Problem(
            URI(
                "userOrPasswordAreInvalid.com"
            )
        )

        val invalidRequestContent = Problem(
            URI(
                "invalidRequestContent.com"
            )
        )

        val userWithUsernameNotFound = Problem(
            URI(
                "userWithUsernameNotFound.com"
            )
        )

        val unknownInternalException = Problem(
            URI(
                "unknownInternalException.com"
            )
        )

        val lobbyNotFound = Problem(
            URI(
                "lobbyNotFound.com"
            )
        )

        val lobbyFull = Problem(
            URI(
                "lobbyFull.com"
            )
        )

        val userAlreadyInLobby = Problem(
            URI(
                "userAlreadyInLobby.com"
            )
        )
    }
}
