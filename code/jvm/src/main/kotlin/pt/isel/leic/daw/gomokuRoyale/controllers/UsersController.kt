package pt.isel.leic.daw.gomokuRoyale.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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