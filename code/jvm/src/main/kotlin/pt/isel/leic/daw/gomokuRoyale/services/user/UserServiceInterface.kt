package pt.isel.leic.daw.gomokuRoyale.services.user

import pt.isel.leic.daw.gomokuRoyale.domain.user.User

interface UserServiceInterface {
    fun registerUser(username: String, email: String, password: String): User

    fun loginUser(username: String?, email: String?, password: String): User

    fun createToken()

    // ...
}