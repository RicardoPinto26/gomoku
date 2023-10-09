package pt.isel.leic.daw.gomokuRoyale.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.isel.leic.daw.gomokuRoyale.controllers.model.AuthenticatedUser
import pt.isel.leic.daw.gomokuRoyale.controllers.model.Problem
import pt.isel.leic.daw.gomokuRoyale.controllers.model.UserCreateInputModel
import pt.isel.leic.daw.gomokuRoyale.controllers.model.UserCreateTokenInputModel
import pt.isel.leic.daw.gomokuRoyale.controllers.model.UserCreateTokenOutputModel
import pt.isel.leic.daw.gomokuRoyale.controllers.model.UserGetStatisticsOutputModel
import pt.isel.leic.daw.gomokuRoyale.services.user.GetUserStatsError
import pt.isel.leic.daw.gomokuRoyale.services.user.TokenCreationError
import pt.isel.leic.daw.gomokuRoyale.services.user.UserCreationError
import pt.isel.leic.daw.gomokuRoyale.services.user.UsersService
import pt.isel.leic.daw.gomokuRoyale.utils.Failure
import pt.isel.leic.daw.gomokuRoyale.utils.Success

@RestController
@RequestMapping("/users")
class UsersController {

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
}