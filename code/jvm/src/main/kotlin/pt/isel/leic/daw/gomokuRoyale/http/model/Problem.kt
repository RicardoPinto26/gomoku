package pt.isel.leic.daw.gomokuRoyale.http.model

import org.springframework.http.ResponseEntity
import java.net.URI

class Problem(
    typeUri: URI
) {
    val type = typeUri.toASCIIString()

    companion object {
        private const val MEDIA_TYPE = "application/problem+json"
        fun response(status: Int, problem: Problem) = ResponseEntity
            .status(status)
            .header("Content-Type", MEDIA_TYPE)
            .body<Any>(problem)

        val userAlreadyExists = Problem(
            URI(
                "userAlreadyExists.com"
            )
        )

        val userOrPasswordAreInvalid = Problem(
                URI(
                        "userOrPasswordAreInvalid.com"
                )
        )

        val insecurePassword = Problem(
            URI(
                "insecurePassword.com"
            )
        )

        val invalidUsername = Problem(
            URI(
                "invalidUsername.com"
            )
        )

        val invalidEmail = Problem(
                URI(
                        "invalidEmail.com"
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

        val lobbyDoesNotExist = Problem(
            URI(
                "lobbyDoesNotExist.com"
            )
        )

        val lobbyNotFull = Problem(
            URI(
                "lobbyNotFull.com"
            )
        )

        val lobbyAlreadyHasGame = Problem(
            URI(
                "lobbyAlreadyHasGame.com"
            )
        )

        val userNotInLobby = Problem(
            URI(
                "userNotInLobby.com"
            )
        )

        val gameAlreadyEnded = Problem(
            URI(
                "gameAlreadyEnded.com"
            )
        )

        val gameDoesNotExist = Problem(
            URI(
                "gameDoesNotExist.com"
            )
        )

        val userNotInGame = Problem(
            URI(
                "userNotInGame.com"
            )
        )

        val gameAlreadyStarted = Problem(
            URI(
                "gameAlreadyStarted.com"
            )
        )

        val userAlreadyInALobby = Problem(
            URI(
                "userAlreadyInALobby.com"
            )
        )
    }
}
