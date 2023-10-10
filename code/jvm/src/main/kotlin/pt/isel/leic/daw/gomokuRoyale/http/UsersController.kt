package pt.isel.leic.daw.gomokuRoyale.http

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.isel.leic.daw.gomokuRoyale.domain.AuthenticatedUser
import pt.isel.leic.daw.gomokuRoyale.http.model.Problem
import pt.isel.leic.daw.gomokuRoyale.http.model.UserCreateInputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.UserCreateTokenInputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.UserCreateTokenOutputModel
import pt.isel.leic.daw.gomokuRoyale.http.model.UserGetStatisticsOutputModel
import pt.isel.leic.daw.gomokuRoyale.services.users.GetUserStatsError
import pt.isel.leic.daw.gomokuRoyale.services.users.TokenCreationError
import pt.isel.leic.daw.gomokuRoyale.services.users.UserCreationError
import pt.isel.leic.daw.gomokuRoyale.services.users.UsersService
import pt.isel.leic.daw.gomokuRoyale.utils.Failure
import pt.isel.leic.daw.gomokuRoyale.utils.Success

@RestController
@RequestMapping("/users")
class UsersController(
    private val usersService: UsersService
) {

    @PostMapping(Uris.Users.CREATE)
    fun createUser(@RequestBody input: UserCreateInputModel): ResponseEntity<*> {
        return when (val res = usersService.registerUser(input.username, input.email, input.password)) {
            is Success -> ResponseEntity.status(201)
                .header(
                    "Location",
                    Uris.Users.byId(res.value).toASCIIString()
                ).build<Unit>()

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
        return when (val res = usersService.createToken(input.username, input.password)) {
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
        usersService.revokeToken(user.token)
    }

    @GetMapping("/user")
    fun getStatistics(@RequestParam username: String): ResponseEntity<*> {
        return when (val res = usersService.getStats(username)) {
            is Success ->
                ResponseEntity.status(200)
                    .body(UserGetStatisticsOutputModel(res.value))

            is Failure -> when (res.value) {
                GetUserStatsError.NoSuchUser ->
                    Problem.response(404, Problem.userWithUsernameNotFound)
            }
        }
    }
}