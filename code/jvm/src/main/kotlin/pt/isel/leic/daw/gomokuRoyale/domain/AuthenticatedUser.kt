package pt.isel.leic.daw.gomokuRoyale.domain

import pt.isel.leic.daw.gomokuRoyale.domain.user.User

class AuthenticatedUser(
    val user: User,
    val token: String
)
