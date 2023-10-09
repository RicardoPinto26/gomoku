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

    @PostMapping("/create")
    fun createUser() {
        TODO()
    }

    @GetMapping("/tokens")
    fun getTokens() {
        TODO()
    }

    @PostMapping("/tokens")
    fun createToken() {
        TODO()
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