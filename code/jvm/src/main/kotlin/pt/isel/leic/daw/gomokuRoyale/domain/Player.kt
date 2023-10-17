package pt.isel.leic.daw.gomokuRoyale.domain

import pt.isel.leic.daw.gomokuRoyale.domain.user.User

sealed interface Player {
    val user: User
}

/*
 * Only makes sense when dealing with Openings that require players to choose a color
 */
// data class UnassignedPlayer(override val user: User) : Player
data class BlackPlayer(override val user: User) : Player
data class WhitePlayer(override val user: User) : Player
