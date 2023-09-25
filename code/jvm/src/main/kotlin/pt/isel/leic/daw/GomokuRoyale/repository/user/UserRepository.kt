package pt.isel.leic.daw.GomokuRoyale.repository.user

import org.springframework.asm.Handle
import pt.isel.leic.daw.GomokuRoyale.domain.user.User

class UserRepository(private val handle: Handle): UserInterface {
    override fun createUser(username: String, email: String, password: String): Int {
        TODO("Not yet implemented")
    }

    override fun getUserByEmail(email: String): User? {
        TODO("Not yet implemented")
    }

    override fun getUserByUsername(username: String): User? {
        TODO("Not yet implemented")
    }

    override fun isUserStoredByUsername(username: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun checkEmailAlreadyExists(email: String): Boolean {
        TODO("Not yet implemented")
    }

}