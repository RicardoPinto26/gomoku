package pt.isel.leic.daw.GomokuRoyale.repository


interface TransactionManager {
    fun <R> run(block: (Transaction) -> R): R
}