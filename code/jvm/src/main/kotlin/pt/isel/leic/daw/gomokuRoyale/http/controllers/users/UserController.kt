package pt.isel.leic.daw.gomokuRoyale.http.controllers.users

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.isel.leic.daw.gomokuRoyale.domain.AuthenticatedUser
import pt.isel.leic.daw.gomokuRoyale.domain.user.UserDomain
import pt.isel.leic.daw.gomokuRoyale.http.controllers.users.models.UserCreateInputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.users.models.UserCreateOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.users.models.UserCreateTokenInputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.users.models.UserCreateTokenOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.controllers.users.models.UserGetStatisticsOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.media.siren.SirenEntity
import pt.isel.leic.daw.gomokuRoyale.http.media.siren.SubEntity
import pt.isel.leic.daw.gomokuRoyale.http.utils.Actions
import pt.isel.leic.daw.gomokuRoyale.http.utils.Links
import pt.isel.leic.daw.gomokuRoyale.http.utils.Rels
import pt.isel.leic.daw.gomokuRoyale.http.utils.Uris
import pt.isel.leic.daw.gomokuRoyale.http.utils.toResponse
import pt.isel.leic.daw.gomokuRoyale.services.users.UserService
import pt.isel.leic.daw.gomokuRoyale.utils.Failure
import pt.isel.leic.daw.gomokuRoyale.utils.Success
import kotlin.time.DurationUnit

/**
 * Controller that handles the requests to the /users endpoint
 *
 * @property userService services that deals with the user business logic
 */
@RestController
class UserController(
    private val userService: UserService,
    private val userDomain: UserDomain
) {
    @GetMapping(Uris.Users.HOME)
    fun getUserHome(): SirenEntity<Unit> {
        return SirenEntity(
            `class` = listOf(Rels.USER_HOME),
            actions = listOf(
                Actions.logout,
                Actions.seekLobby
                // Actions.listLobbies
            ),
            links = listOf(
                Links.self(Uris.userHome())
            )
        )
    }

    /**
     * Handles the request to create a user.
     *
     * @param input the [UserCreateInputModel] with the user information
     *
     * @return the response to the request with the [UserCreateOutputModel] in the body or an error value
     */
    @PostMapping(Uris.Users.PREFIX)
    fun createUser(@RequestBody input: UserCreateInputModel, response: HttpServletResponse): ResponseEntity<*> {
        logger.info("Starting registration of user {}", input.username)
        val res = userService.registerUser(input.username, input.email, input.password)
        logger.info("Result of registration of user {}: {}", input.username, res)
        return when (res) {
            is Success -> {
                ResponseEntity.status(201)
                    .header(
                        "Location",
                        Uris.Users.byUsername(res.value.username).toASCIIString()
                    ).body(
                        SirenEntity(
                            `class` = listOf(Rels.REGISTER),
                            properties = UserCreateOutputModel(res.value),
                            links = listOf(
                                Links.userHome
                            )
                        )
                    )
            }

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
        @RequestBody input: UserCreateTokenInputModel,
        response: HttpServletResponse
    ): ResponseEntity<*> {
        return when (val res = userService.createToken(input.username, input.password)) {
            is Success -> {
                sendTokenCookie(response, res.value.tokenValue)
                ResponseEntity.status(201)
                    .body(
                        SirenEntity(
                            `class` = listOf(Rels.LOGIN),
                            properties = UserCreateTokenOutputModel(res.value.tokenValue),
                            links = listOf(
                                Links.userHome
                            )
                        )
                    )
            }

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
        user: AuthenticatedUser,
        response: HttpServletResponse
    ): SirenEntity<Unit> {
        userService.revokeToken(user.token)

        revokeTokenCookie(response)

        return SirenEntity(
            `class` = listOf(Rels.LOGOUT),
            links = listOf(
                Links.home
            )
        )
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
                    .body(
                        SirenEntity(
                            `class` = listOf(Rels.USER),
                            properties = UserGetStatisticsOutputModel(res.value),
                            links = listOf(
                                Links.self(Uris.Users.byUsername(username))
                            )
                        )
                    )
            }

            is Failure -> {
                logger.info("Failed request")
                res.value.toResponse()
            }
        }
    }

    @GetMapping(Uris.Users.PREFIX)
    fun getUsers(
        @RequestParam(defaultValue = "0") skip: Int,
        @RequestParam(defaultValue = "100" /*TODO: HARDCODED*/) limit: Int
    ): ResponseEntity<*> {
        logger.info("Request received for users ranking")
        return when (val res = userService.getUsersRanking(skip, limit)) {
            is Success -> {
                logger.info("Success request")
                ResponseEntity.status(200)
                    .body(
                        SirenEntity<Unit>(
                            `class` = listOf(Rels.LIST_USERS),
                            entities = res.value.users.map {
                                SubEntity.EmbeddedSubEntity(
                                    `class` = listOf(Rels.USER),
                                    rel = listOf(Rels.ITEM, Rels.USER),
                                    properties = UserGetStatisticsOutputModel(it),
                                    links = listOf(
                                        Links.self(Uris.Users.byUsername(it.username))
                                    )
                                )
                            }
                        )
                    )
            }

            is Failure -> {
                logger.info("Failed request")
                res.value.toResponse()
            }
        }
    }

    private fun sendTokenCookie(response: HttpServletResponse, token: String) {
        val cookie = Cookie("token", token)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.setAttribute("SameSite", "Strict")
        cookie.maxAge = userDomain.config.tokenTtl.toInt(DurationUnit.SECONDS)
        response.addCookie(cookie)
    }

    private fun revokeTokenCookie(response: HttpServletResponse) {
        val cookie = Cookie("token", null)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.setAttribute("SameSite", "Strict")
        cookie.maxAge = 0
        response.addCookie(cookie)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UserController::class.java)
    }
}
