package pt.isel.leic.daw.gomokuRoyale.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.leic.daw.gomokuRoyale.controllers.models.CreateUserRequest
import pt.isel.leic.daw.gomokuRoyale.controllers.models.CreateUserResponse
import pt.isel.leic.daw.gomokuRoyale.services.dtos.user.RegisterInputDTO
import pt.isel.leic.daw.gomokuRoyale.services.user.UserServiceInterface

@RestController
@RequestMapping("/users")
class UsersController(private val usersService: UserServiceInterface) {

    @PostMapping("")
    fun createUser(@RequestBody cur: CreateUserRequest): CreateUserResponse {
        val user = usersService.registerUser(RegisterInputDTO(cur.username, cur.email, cur.password))
        return CreateUserResponse(user.userId, user.username)
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