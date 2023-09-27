package pt.isel.leic.daw.gomokuRoyale.domain

import pt.isel.leic.daw.gomokuRoyale.domain.user.User

data class Game(
        private val id: Int,
        private val name: String,
        private val whitePlayer: User,
        private val blackPlayer: User,
        private val settings: GameSettings
) {
    private val board: Board = BoardRun(settings.boardSize, settings.winningLength, settings.overflowAllowed)
}