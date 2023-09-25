package pt.isel.leic.daw.GomokuRoyale.repository.jdbi

import org.jdbi.v3.core.Handle
import pt.isel.leic.daw.GomokuRoyale.repository.Transaction
import pt.isel.leic.daw.GomokuRoyale.repository.user.JdbiUsersRepository

class JdbiTransaction(
    private val handle: Handle
) : Transaction {

    override val usersRepository: JdbiUsersRepository = JdbiUsersRepository(handle)
    //override val tokenRepository: JdbiTokensRepository = JdbiTokensRepository(handle)

    override fun rollback() {
        handle.rollback()
    }
}