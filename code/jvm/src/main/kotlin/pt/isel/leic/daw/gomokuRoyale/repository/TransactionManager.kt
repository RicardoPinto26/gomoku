package pt.isel.leic.daw.gomokuRoyale.repository


interface TransactionManager {
    fun <R> run(block: (Transaction) -> R): R
}