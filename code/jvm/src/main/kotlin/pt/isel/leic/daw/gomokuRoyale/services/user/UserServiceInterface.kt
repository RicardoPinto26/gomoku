package pt.isel.leic.daw.gomokuRoyale.services.user

import pt.isel.leic.daw.gomokuRoyale.domain.user.User

interface UserServiceInterface {

    /**
    Creates a new user
    @param user: RegisterInputDTO
    @throws UserAlreadyExistsException
    @throws InvalidArgumentsException
    @return RegisterOutputDTO
     */

    fun registerUser(username: String, email: String, password: String): UserCreationResult

    /**
     *  Logs in a user
     *  @param username, user username
     *  @param email, user email
     *  @param password, user password
     *  @throws Exception
     *  @return User
     */

    fun loginUser(username: String?, email: String?, password: String): User


    /**
     *  Creates a token for a user
     *  @param userId user unique identifier
     *
     *  @return the token
     */
    fun createToken(username: String, password: String): TokenCreationResult


    /**
     *  Gets user's statistics
     *  @param username name of the user whose stats are being requested
     *  @return Player statistics or error
     */
    fun getStats(username: String): GetUserStatsResult

    /**
     * Gets user by token
     * @param token token of the user
     *
     * @return User if the token is valid, null if not
     */
    fun getUserByToken(token: String): User?


    /**
     * Revokes a token
     * @param token of the user
     *
     * @return true if the token was revoked, false if not
     */
    fun revokeToken(token: String): Boolean
}
