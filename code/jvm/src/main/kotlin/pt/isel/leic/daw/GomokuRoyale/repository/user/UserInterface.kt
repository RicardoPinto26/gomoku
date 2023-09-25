package pt.isel.leic.daw.GomokuRoyale.repository.user

import pt.isel.leic.daw.GomokuRoyale.domain.user.User

interface UserInterface {
    fun createUser(username: String, email: String, password: String): Int

    fun isUserStoredByUsername(username: String): Boolean

    fun checkEmailAlreadyExists(email: String): Boolean

    fun getUserByUsername(username: String): User?

    fun getUserByEmail(email: String): User?

    /*
          fun loginUserByUsername(username: String, password: String): User

          fun loginUserByEmail(email: String, password: String): User
     */

}