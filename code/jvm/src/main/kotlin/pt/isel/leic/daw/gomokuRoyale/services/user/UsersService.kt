package pt.isel.leic.daw.gomokuRoyale.services.user

import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.repository.jdbi.JdbiTransactionManager

class UsersService(
    private val transactionManager: JdbiTransactionManager,
    private val userDomain: User
) : UserServiceInterface {

    override fun registerUser(username: String, email: String, password: String): User {
        userDomain.checkUserCredentials(username, email, password)
        val hashedPassword = userDomain.hashPassword(password)

        val id = transactionManager.run {
            val userRepo = it.usersRepository
            if (userRepo.isUserStoredByUsername(username) || userRepo.isUserStoredByEmail(email)) {
                throw IllegalAccessException() // Arranjar uma exception melhor
            } else {
                userRepo.createUser(username, email, password)
            }
        }

        return User(id, username, email, hashedPassword) // Sq criar uma dto para n estar a mandar a pass
    }

    override fun loginUser(username: String?, email: String?, password: String): User {
        TODO("Not yet implemented")
    }

    override fun createToken() {
        TODO("Not yet implemented")
    }
}
