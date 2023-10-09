package pt.isel.leic.daw.gomokuRoyale.services.user

import org.springframework.stereotype.Component
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.repository.jdbi.JdbiTransactionManager
import pt.isel.leic.daw.gomokuRoyale.services.dtos.user.RegisterInputDTO
import pt.isel.leic.daw.gomokuRoyale.services.dtos.user.RegisterOutputDTO
import pt.isel.leic.daw.gomokuRoyale.services.exceptions.UserAlreadyExistsException
import pt.isel.leic.daw.gomokuRoyale.services.exceptions.UserInvalidCredentialsException

@Component
class UsersService(
    private val transactionManager: JdbiTransactionManager,
    private val userDomain: User
) : UserServiceInterface {

    override fun registerUser(user : RegisterInputDTO): RegisterOutputDTO {
        userDomain.checkUserCredentialsRegister(user.username, user.email, user.password)
        val hashedPassword = userDomain.hashPassword(user.password)

        val id = transactionManager.run {
            val userRepo = it.usersRepository
            if (userRepo.isUserStoredByUsername(user.username)) {
                throw UserAlreadyExistsException("User with username ${user.username} already exists")
            }
            if(userRepo.isUserStoredByEmail(user.email)) {
                throw UserAlreadyExistsException("User with email ${user.email} already exists")
            } else {
                userRepo.createUser(user.username, user.email, hashedPassword)
            }
        }
        return RegisterOutputDTO(id, user.username)
    }

    override fun loginUser(username: String?, email: String?, password: String): User {
        userDomain.checkUserCredentialsLogin(username, email, password)

        val user = transactionManager.run {
            val userRepo = it.usersRepository
            if (username != null) {
                userRepo.getUserByUsername(username)
            } else {
                userRepo.getUserByEmail(email!!)
            }
        }
        if (user == null || !userDomain.checkPassword(password, user.hashPassword)) {
            throw UserInvalidCredentialsException("Invalid credentials")
        }
        return user //create dto
    }

    override fun createToken() {
        TODO("Not yet implemented")
    }
}
