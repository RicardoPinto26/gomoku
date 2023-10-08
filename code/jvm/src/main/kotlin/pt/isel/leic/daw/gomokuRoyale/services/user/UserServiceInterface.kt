package pt.isel.leic.daw.gomokuRoyale.services.user

import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import pt.isel.leic.daw.gomokuRoyale.services.dtos.user.RegisterInputDTO
import pt.isel.leic.daw.gomokuRoyale.services.dtos.user.RegisterOutputDTO

interface UserServiceInterface {

    /**
    Creates a new user
    @param user: RegisterInputDTO
    @throws UserAlreadyExistsException
    @throws InvalidArgumentsException
    @return RegisterOutputDTO
     */

    fun registerUser(user: RegisterInputDTO): RegisterOutputDTO

    /**
     *  Logs in a user
     *  @param username, user username
     *  @param email, user email
     *  @param password, user password
     *  @throws Exception
     *  @return User
     */

    fun loginUser(username: String?, email: String?, password: String): User


    fun createToken()

    // ...
}
