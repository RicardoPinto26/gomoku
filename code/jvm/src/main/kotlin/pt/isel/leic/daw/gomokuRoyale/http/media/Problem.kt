package pt.isel.leic.daw.gomokuRoyale.http.media

import org.springframework.http.ResponseEntity

/**
 * Entity for HTTP request problems
 *
 * @property type the type of problem
 * @property title title of the problem
 * @property status the HTTP status code of the problem
 * @property detail a more thorough description of the problem
 */
open class Problem(
    val type: String,
    val title: String,
    val status: Int,
    val detail: String
) {
    companion object {
        const val MEDIA_TYPE = "application/problem+json"
        fun response(problem: Problem) = ResponseEntity
            .status(problem.status)
            .header("Content-Type", MEDIA_TYPE)
            .body<Any>(problem)

        val notAuthenticatedString = """
            {
                "type": "notAuthenticated.com",
                "title": "Not authenticated",
                "status": 401,
                "detail": "You need to provide authentication credentials to access this resource"
            }
        """.trimIndent()

        val userAlreadyExists = Problem(
            "userAlreadyExists.com",
            "User already exists",
            409,
            "A user with the same username or email already exists"
        )

        val userOrPasswordIsIncorrect = Problem(
            "userOrPasswordAreInvalid.com",
            "The user or the password is incorrect",
            404,
            "A user with that username and password does not exist"
        )

        val insecurePassword = Problem(
            "insecurePassword.com",
            "The password is insecure",
            400,
            "The password is insecure. A password must contain at least 8 characters, one uppercase letter, one lowercase letter, and one number"
        )

        val invalidUsername = Problem(
            "invalidUsername.com",
            "The username is not valid",
            400,
            "The username is not valid. A username must contain at least 3 characters and at most 50 characters"
        )

        val tokenNotFound = Problem(
            "tokenNotFound.com",
            "The token does not exist",
            401,
            "The token does not exist"
        )

        val invalidEmail = Problem(
            "invalidEmail.com",
            "The mail is not valid",
            400,
            "The mail is not valid, an email must be in the format: abc@def.ghi"
        )

        val noUsersFound = Problem(
            "noUsersFound.com",
            "No users found",
            404,
            "No users found"
        )

        val invalidRequestContent = Problem(
            "invalidRequestContent.com",
            "The request content is not valid",
            400,
            "The request content is not valid"
        )

        val userWithUsernameNotFound = Problem(
            "userWithUsernameNotFound.com",
            "A user with that username does not exist",
            404,
            "A user with that username does not exist"
        )

        val unknownInternalException = Problem(
            "unknownInternalException.com",
            "An unknown internal exception occurred",
            500,
            "An unknown internal exception occurred, please try again later"
        )

        val lobbyNotFound = Problem(
            "lobbyNotFound.com",
            "A Lobby with that id does not exist",
            404,
            "A Lobby with that id does not exist"
        )

        val lobbyFull = Problem(
            "lobbyFull.com",
            "The lobby is full",
            409,
            "The lobby is already full"
        )

        val userAlreadyInLobby = Problem(
            "userAlreadyInLobby.com",
            "User already in the lobby",
            409,
            "You're already in that lobby"
        )

        val lobbyDoesNotExist = Problem(
            "lobbyDoesNotExist.com",
            "A lobby with that id does not exist",
            404,
            "A lobby with that id does not exist"
        )

        val lobbyAlreadyHasGame = Problem(
            "lobbyAlreadyHasGame.com",
            "The lobby already has a game",
            409,
            "The lobby already has a game"
        )

        val userNotInLobby = Problem(
            "userNotInLobby.com",
            "You don't have access to that lobby",
            403,
            "You don't have access to that lobby"
        )

        val gameAlreadyEnded = Problem(
            "gameAlreadyEnded.com",
            "The game has already ended",
            409,
            "The game has already ended"
        )

        val gameDoesNotExist = Problem(
            "gameDoesNotExist.com",
            "Game with that id does not exist",
            404,
            "Game with that id does not exist"
        )

        val userNotInGame = Problem(
            "userNotInGame.com",
            "User doesn't have access to that game",
            403,
            "You don't have access to that game"
        )

        @Suppress("unused")
        class UserAlreadyInALobbyProblem(
            type: String,
            title: String,
            status: Int,
            detail: String,
            val lobbyID: Int,
            val gameID: Int
        ) : Problem(type, title, status, detail)

        val userAlreadyInALobby = { lobbyID: Int, gameID: Int ->
            UserAlreadyInALobbyProblem(
                "userAlreadyInALobbyProblem",
                "User is already in a Lobby",
                409,
                "You're already in a lobby. Please finish that game to join another.",
                lobbyID,
                gameID
            )
        }

        val invalidPosition = Problem(
            "invalidPosition.com",
            "The position is not valid",
            400,
            "The position is not valid"
        )

        val occupiedPosition = Problem(
            "occupiedPosition.com",
            "The position is already occupied",
            409,
            "The position is already occupied"
        )

        val wrongTurn = Problem(
            "wrongTurn.com",
            "It's not that user's turn",
            409,
            "It's not your turn"
        )
        val invalidColor = Problem(
            "invalidColor.com",
            "Invalid Color",
            409,
            "Invalid Color"
        )
        val invalidNextMove = Problem(
            "invalidNextMove.com",
            "Invalid Next Move (Opening)",
            409,
            "Invalid Next Move"
        )
        val chooseCorrectActionColor = Problem(
            "chooseCorrectActionColor.com",
            "Choose the piece color",
            409,
            "Choose the piece color you want to play with"
        )
        val chooseCorrectActionPlacePiece = Problem(
            "chooseCorrectActionPlacePiece.com",
            "Choose the position to place the piece",
            409,
            "Choose the position to place the piece"
        )
        val chooseCorrectActionNextMove = Problem(
            "chooseCorrectActionNextMove.com",
            "Choose the variant of the opening",
            409,
            "Choose the variant of the opening"
        )

        val opponentAlreadyJoined = Problem(
            "opponentAlreadyJoined.com",
            "The opponent already joined the lobby",
            409,
            "The opponent already joined the lobby"
        )
    }
}
