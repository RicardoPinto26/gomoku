package pt.isel.leic.daw.gomokuRoyale.repository.jdbi

import org.jdbi.v3.core.Jdbi
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.repository.Transaction
import pt.isel.leic.daw.gomokuRoyale.repository.TransactionManager

@Component
class JdbiTransactionManager(
    private val jdbi: Jdbi
) : TransactionManager {
    override fun <R> run(block: (Transaction) -> R): R {
        logger.info("Entering inTransaction")
        try {
            return jdbi.inTransaction<R, Exception> { handle ->
                logger.info("Creating JdbiTransaction")
                val transaction = JdbiTransaction(handle)
                logger.info("After JdbiTransaction")
                logger.info("Executing block")
                block(transaction)
            }
        } catch (e: Exception) {
            logger.error("Error in transaction", e)
            throw e
        } finally {
            logger.info("Exiting inTransaction")
        }
    }

    companion object {
        val logger = LoggerFactory.getLogger(JdbiTransactionManager::class.java)
    }
}
