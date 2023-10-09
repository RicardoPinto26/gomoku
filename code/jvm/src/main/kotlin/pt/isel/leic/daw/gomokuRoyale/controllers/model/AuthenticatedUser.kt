package pt.isel.leic.daw.gomokuRoyale.controllers.model

import pt.isel.leic.daw.gomokuRoyale.domain.user.User

class AuthenticatedUser(
    val user: User,
    val token: String
)
