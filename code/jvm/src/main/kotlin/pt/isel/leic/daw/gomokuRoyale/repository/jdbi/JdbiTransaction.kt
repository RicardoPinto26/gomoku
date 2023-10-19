package pt.isel.leic.daw.gomokuRoyale.repository.jdbi

import org.jdbi.v3.core.Handle
import pt.isel.leic.daw.gomokuRoyale.repository.Transaction
import pt.isel.leic.daw.gomokuRoyale.repository.game.GameRepositoryJDBI
import pt.isel.leic.daw.gomokuRoyale.repository.lobby.LobbyRepositoryJDBI
import pt.isel.leic.daw.gomokuRoyale.repository.user.UserRepositoryJDBI

class JdbiTransaction(
    private val handle: Handle
) : Transaction {

    override val userRepository: UserRepositoryJDBI
        get() = UserRepositoryJDBI(handle)

    override val gameRepository: GameRepositoryJDBI
        get() = GameRepositoryJDBI(handle)

    override val lobbyRepository: LobbyRepositoryJDBI
        get() = LobbyRepositoryJDBI(handle)

    override fun rollback() {
        handle.rollback()
    }
}
