package pt.isel.leic.daw.gomokuRoyale.http.controllers

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import pt.isel.leic.daw.gomokuRoyale.domain.AuthenticatedUser
import pt.isel.leic.daw.gomokuRoyale.http.Uris
import pt.isel.leic.daw.gomokuRoyale.http.model.Problem
import pt.isel.leic.daw.gomokuRoyale.http.model.user.UserCreateInputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.user.UserCreateOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.user.UserCreateTokenInputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.user.UserCreateTokenOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.user.UserGetStatisticsOutputModel
import pt.isel.leic.daw.gomokuRoyale.services.users.GetUserStatsError
import pt.isel.leic.daw.gomokuRoyale.services.users.TokenCreationError
import pt.isel.leic.daw.gomokuRoyale.services.users.UserCreationError
import pt.isel.leic.daw.gomokuRoyale.services.users.UserService
import pt.isel.leic.daw.gomokuRoyale.utils.Failure
import pt.isel.leic.daw.gomokuRoyale.utils.Success

@RestController
class UserController(
    private val userService: UserService
) {

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

            is Failure -> when (res.value) {
                UserCreationError.InsecurePassword -> Problem.response(400, Problem.insecurePassword)
                UserCreationError.UserAlreadyExists -> Problem.response(409, Problem.userAlreadyExists)
            }
        }
    }

    @PostMapping(Uris.Users.TOKEN)
    fun createToken(
        @RequestBody input: UserCreateTokenInputModel
    ): ResponseEntity<*> {
        return when (val res = userService.createToken(input.username, input.password)) {
            is Success ->
                ResponseEntity.status(201)
                    .body(UserCreateTokenOutputModel(res.value.tokenValue))

            is Failure -> when (res.value) {
                TokenCreationError.UserOrPasswordAreInvalid ->
                    Problem.response(400, Problem.userOrPasswordAreInvalid)
            }
        }
    }

    @PostMapping(Uris.Users.LOGOUT)
    fun logout(
        user: AuthenticatedUser
    ) {
        userService.revokeToken(user.token)
    }

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
                when (res.value) {
                    GetUserStatsError.NoSuchUser ->
                        Problem.response(404, Problem.userWithUsernameNotFound)
                }
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UserController::class.java)
    }
}
