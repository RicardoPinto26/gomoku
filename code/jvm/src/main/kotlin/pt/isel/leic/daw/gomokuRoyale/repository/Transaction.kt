package pt.isel.leic.daw.gomokuRoyale.repository

import pt.isel.leic.daw.gomokuRoyale.repository.game.GameRepository
import pt.isel.leic.daw.gomokuRoyale.repository.game.GameStateRepository
import pt.isel.leic.daw.gomokuRoyale.repository.lobby.LobbyRepository
import pt.isel.leic.daw.gomokuRoyale.repository.user.UsersRepository

interface Transaction {
    val usersRepository: UsersRepository
    val gameRepository: GameRepository
    val gameStateRepository: GameStateRepository
    val lobbyRepository: LobbyRepository

    fun rollback()
}
