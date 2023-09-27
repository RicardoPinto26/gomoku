package pt.isel.leic.daw.gomokuRoyale.repository

import pt.isel.leic.daw.gomokuRoyale.repository.user.JdbiUsersRepository

interface Transaction {
    val usersRepository: JdbiUsersRepository
    fun rollback()
}