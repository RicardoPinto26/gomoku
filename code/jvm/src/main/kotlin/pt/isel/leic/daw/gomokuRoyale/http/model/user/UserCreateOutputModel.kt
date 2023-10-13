package pt.isel.leic.daw.gomokuRoyale.http.model.user

import pt.isel.leic.daw.gomokuRoyale.services.users.UserExternalInfo

data class UserCreateOutputModel(
    val username: String,
    val email: String,
    val rating: Int,
    val gamesPlayed: Int
) {
    constructor(uei: UserExternalInfo) : this(uei.username, uei.email, uei.rating, uei.gamesPlayed)
}
