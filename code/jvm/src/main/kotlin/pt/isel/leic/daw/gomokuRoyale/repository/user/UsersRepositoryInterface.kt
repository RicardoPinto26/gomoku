package pt.isel.leic.daw.gomokuRoyale.repository.user

import pt.isel.leic.daw.gomokuRoyale.domain.user.User

interface UsersRepositoryInterface {
    fun createUser(username: String, email: String, password: String): Int

    fun isUserStoredByUsername(username: String): Boolean

    fun isUserStoredByEmail(email: String): Boolean

    fun getUserByUsername(username: String): User?

    fun getUserByEmail(email: String): User?

    fun getAllUsers(): List<User>

    /*
          fun loginUserByUsername(username: String, password: String): User

          fun loginUserByEmail(email: String, password: String): User
     */

}