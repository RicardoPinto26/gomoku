package pt.isel.leic.daw.gomokuRoyale.http.model.user


/**
 * Token creation input information
 *
 * @property username the username of the user requesting the token
 * @property password the user's password
 *
 */
data class UserCreateTokenInputModel(
    val username: String,
    val password: String
)
