package pt.isel.leic.daw.GomokuRoyale.repository.jdbi

import org.jdbi.v3.core.Jdbi
import pt.isel.leic.daw.GomokuRoyale.repository.Transaction
import pt.isel.leic.daw.GomokuRoyale.repository.TransactionManager


class JdbiTransactionManager(
    private val jdbi: Jdbi
) : TransactionManager {
    override fun <R> run(block: (Transaction) -> R): R =
        jdbi.inTransaction<R, Exception> { handle ->
            val transaction = JdbiTransaction(handle)
            block(transaction)
        }
}