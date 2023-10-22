package pt.isel.leic.daw.gomokuRoyale.http.controllers.users

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.isel.leic.daw.gomokuRoyale.domain.AuthenticatedUser
import pt.isel.leic.daw.gomokuRoyale.http.Uris
import pt.isel.leic.daw.gomokuRoyale.http.controllers.users.models.UserCreateInputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.users.models.UserCreateOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.users.models.UserCreateTokenInputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.users.models.UserCreateTokenOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.users.models.UserGetStatisticsOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.utils.toResponse
import pt.isel.leic.daw.gomokuRoyale.services.users.UserService
import pt.isel.leic.daw.gomokuRoyale.utils.Failure
import pt.isel.leic.daw.gomokuRoyale.utils.Success

/**
 * Controller that handles the requests to the /users endpoint
 *
 * @property userService services that deals with the user business logic
 */
@RestController
class UserController(
    private val userService: UserService
) {

    /**
     * Handles the request to create a user.
     *
     * @param input the [UserCreateInputModel] with the user information
     *
     * @return the response to the request with the [UserCreateOutputModel] in the body or an error value
     */
    @PostMapping(Uris.Users.CREATE)
    fun createUser(@RequestBody input: UserCreateInputModel): ResponseEntity<*> {
        logger.info("Starting registration of user {}", input.username)
        val res = userService.registerUser(input.username, input.email, input.password)
        logger.info("Result of registration of user {}: {}", input.username, res)
        return when (res) {
            is Success -> ResponseEntity.status(201)
                .header(
                    "Location",
                    Uris.Users.byUsername(res.value.username).toASCIIString()
                ).body(UserCreateOutputModel(res.value))

            is Failure -> res.value.toResponse()
        }
    }

    /**
     * Handles the request to create a token.
     *
     * @param input the [UserCreateTokenInputModel] with the user information
     *
     * @return the response to the request with the [UserCreateTokenOutputModel] in the body or an error value
     */
    @PostMapping(Uris.Users.TOKEN)
    fun createToken(
        @RequestBody input: UserCreateTokenInputModel
    ): ResponseEntity<*> {
        return when (val res = userService.createToken(input.username, input.password)) {
            is Success ->
                ResponseEntity.status(201)
                    .body(UserCreateTokenOutputModel(res.value.tokenValue))

            is Failure -> res.value.toResponse()
        }
    }

    /**
     * Handles logout request.
     *
     * @param user the [AuthenticatedUser] creating the game
     *
     */
    @PostMapping(Uris.Users.LOGOUT)
    fun logout(
        user: AuthenticatedUser
    ) {
        userService.revokeToken(user.token)
    }

    /**
     * Handles user's information request.
     *
     * @param username the name of the user whose information is being requested
     *
     * @return the response to the request with the [UserGetStatisticsOutputModel] in the body or an error value
     */
    @GetMapping(Uris.Users.DETAILS)
    fun userDetails(@PathVariable username: String): ResponseEntity<*> {
        logger.info("Request received for user $username details")
        return when (val res = userService.getStats(username)) {
            is Success -> {
                logger.info("Success request")
                ResponseEntity.status(200)
                    .body(UserGetStatisticsOutputModel(res.value))
            }

            is Failure -> {
                logger.info("Failed request")
                res.value.toResponse()
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UserController::class.java)
    }
}