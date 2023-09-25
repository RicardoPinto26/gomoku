package pt.isel.leic.daw.GomokuRoyale.repository

import pt.isel.leic.daw.GomokuRoyale.repository.user.JdbiUsersRepository

interface Transaction {
    val usersRepository: JdbiUsersRepository
    fun rollback()
}