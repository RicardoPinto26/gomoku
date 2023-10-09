package pt.isel.leic.daw.gomokuRoyale.controllers.models

data class CreateUserRequest(
    val username: String,
    val email: String,
    val password: String
)