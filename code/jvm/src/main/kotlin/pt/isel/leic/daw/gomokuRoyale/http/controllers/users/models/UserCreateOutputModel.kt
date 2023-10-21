package pt.isel.leic.daw.gomokuRoyale.http.controllers.users.models

import pt.isel.leic.daw.gomokuRoyale.services.users.UserExternalInfo

/**
 * User creation output information
 *
 * @property username the username of the user created
 * @property email the email of the user created
 * @property rating the user's initial rating
 * @property gamesPlayed number of games played by the user
 *
 */
data class UserCreateOutputModel(
    val username: String,
    val email: String,
    val rating: Int,
    val gamesPlayed: Int
) {
    constructor(uei: UserExternalInfo) : this(uei.username, uei.email, uei.rating, uei.gamesPlayed)
}
