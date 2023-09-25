package pt.isel.leic.daw.GomokuRoyale.repository.user

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import pt.isel.leic.daw.GomokuRoyale.domain.user.User

class JdbiUsersRepository(private val handle: Handle) : UsersRepositoryInterface {

    override fun createUser(username: String, email: String, password: String): Int =
        handle.createUpdate(
            """
            insert into users (username, email, password) values (:username, :email, :password)
            """
        )
            .bind("username", username)
            .bind("email", email)
            .bind("password_validation", password)
            .executeAndReturnGeneratedKeys()
            .mapTo<Int>()
            .one()


    override fun getUserByEmail(email: String): User? =
        handle.createQuery("select * from users where email = :email")
            .bind("email", email)
            .mapTo<User>()
            .singleOrNull()

    override fun getAllUsers(): List<User> =
        handle.createQuery("select * from users")
            .mapTo<User>()
            .toList()


    override fun getUserByUsername(username: String): User? =
        handle.createQuery("select * from users where username = :username")
            .bind("username", username)
            .mapTo<User>()
            .singleOrNull()

    override fun isUserStoredByUsername(username: String): Boolean =
        handle.createQuery(" select exists (select 1 from users where username = :username")
            .bind("username", username)
            .mapTo(Boolean::class.java)
            .first()


    override fun isUserStoredByEmail(email: String): Boolean =
        handle.createQuery("select exists (select 1 from users where email = :email")
            .bind("email", email)
            .mapTo(Boolean::class.java)
            .first()
}