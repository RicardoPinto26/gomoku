package pt.isel.leic.daw.gomokuRoyale.domain

import pt.isel.leic.daw.gomokuRoyale.domain.user.User

/**
 * I can't figure out how to deal with openings, since user1 isn't always WHITE.
 */
data class Game internal constructor(
    private val id: Int,
    private val name: String,
    private val user1: User,
    private val user2: User,
    private val settings: GameSettings,
    private val board: Board,
    private val currentOpeningIndex: Int,
    private val openingFinished: Boolean,
    private val turn: User
) {
}
