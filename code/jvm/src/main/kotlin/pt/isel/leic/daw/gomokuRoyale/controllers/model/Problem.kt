package pt.isel.leic.daw.gomokuRoyale.controllers.model

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
                "example.com"
            )
        )
        val insecurePassword = Problem(
            URI(
                "example.com"
            )
        )

        val userOrPasswordAreInvalid = Problem(
            URI(
                "example.com"
            )
        )

        val invalidRequestContent = Problem(
            URI(
                "example.com"
            )
        )

        val userWithUsernameNotFound = Problem(
            URI(
                "example.com"
            )
        )
    }
}