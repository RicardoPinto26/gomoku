package pt.isel.leic.daw.GomokuRoyale.repository

interface Transaction {
    //val usersRepository: UserRepository
    fun rollback()
}