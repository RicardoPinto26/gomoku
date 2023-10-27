package pt.isel.leic.daw.gomokuRoyale.http.controllers.users.models

/**
 * User creation input information
 *
 * @property username the username of the user to be created
 * @property email the email of the user to be created
 * @property password the password of the user to be created
 *
 */
data class UserCreateInputModel(
    val username: String,
    val email: String,
    val password: String
)
