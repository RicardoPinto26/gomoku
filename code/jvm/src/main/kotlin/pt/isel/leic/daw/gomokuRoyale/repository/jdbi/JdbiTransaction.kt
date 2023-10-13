package pt.isel.leic.daw.gomokuRoyale.repository.jdbi

import org.jdbi.v3.core.Handle
import pt.isel.leic.daw.gomokuRoyale.repository.Transaction
import pt.isel.leic.daw.gomokuRoyale.repository.game.GameRepository
import pt.isel.leic.daw.gomokuRoyale.repository.game.GameStateRepository
import pt.isel.leic.daw.gomokuRoyale.repository.lobby.LobbyRepository
import pt.isel.leic.daw.gomokuRoyale.repository.user.UserRepositoryJDBI

class JdbiTransaction(
    private val handle: Handle
) : Transaction {

    override val usersRepository: UserRepositoryJDBI
        get() = UserRepositoryJDBI(handle)

    override val gameRepository: GameRepository
        get() = GameRepository(handle)

    override val gameStateRepository: GameStateRepository
        get() = GameStateRepository(handle)

    override val lobbyRepository: LobbyRepository
        get() = LobbyRepository(handle)


    override fun rollback() {
        handle.rollback()
    }
}
