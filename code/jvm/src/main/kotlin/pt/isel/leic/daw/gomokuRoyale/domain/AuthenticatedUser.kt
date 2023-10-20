package pt.isel.leic.daw.gomokuRoyale.domain

import pt.isel.leic.daw.gomokuRoyale.domain.user.User

/**
 * An authenticated user entity, i.e a user and his token
 *
 * @property user the user object
 * @property token string containing the token
 */
class AuthenticatedUser(
    val user: User,
    val token: String
)
