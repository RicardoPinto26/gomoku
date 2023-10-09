package pt.isel.leic.daw.gomokuRoyale.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UsersController(
    private val userService: UsersService
) {

    @PostMapping(Uris.Users.CREATE)
    fun createUser(@RequestBody input: UserCreateInputModel): ResponseEntity<*> {
        return when (val res = userService.registerUser(input.username, input.email, input.password)) {
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
        TODO()
    }

    @GetMapping("/user")
    fun getStatistics(@RequestParam username: String): ResponseEntity<*> {
        return when (val res = userService.getStats(username)) {
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