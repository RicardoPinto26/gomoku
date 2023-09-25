package pt.isel.leic.daw.GomokuRoyale.domain

import pt.isel.leic.daw.GomokuRoyale.domain.user.User

data class Game(
        private val id: Int,
        private val name: String,
        private val whitePlayer: User,
        private val blackPlayer: User,
        private val settings: GameSettings
) {
    private val board: Board = Board(settings.boardSize, settings.winningLength, settings.overflowAllowed,Opening.FREESTYLE)
}