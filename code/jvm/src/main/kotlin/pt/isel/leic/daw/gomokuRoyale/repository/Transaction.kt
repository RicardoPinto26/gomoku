package pt.isel.leic.daw.gomokuRoyale.repository

import pt.isel.leic.daw.gomokuRoyale.repository.game.GameRepository
import pt.isel.leic.daw.gomokuRoyale.repository.game.GameStateRepository
import pt.isel.leic.daw.gomokuRoyale.repository.lobby.LobbyRepository
import pt.isel.leic.daw.gomokuRoyale.repository.user.UserRepository

interface Transaction {
    val usersRepository: UserRepository
    val gameRepository: GameRepository
    val gameStateRepository: GameStateRepository
    val lobbyRepository: LobbyRepository

    fun rollback()
}
