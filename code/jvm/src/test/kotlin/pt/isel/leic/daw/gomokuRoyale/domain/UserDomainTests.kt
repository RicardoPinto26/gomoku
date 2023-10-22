package pt.isel.leic.daw.gomokuRoyale.domain

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserInvalidEmail
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserInvalidPassword
import pt.isel.leic.daw.gomokuRoyale.domain.exceptions.UserInvalidUsername
import pt.isel.leic.daw.gomokuRoyale.domain.token.Sha256TokenEncoder
import pt.isel.leic.daw.gomokuRoyale.domain.token.TokenEncoder
import pt.isel.leic.daw.gomokuRoyale.domain.user.UserDomain
import pt.isel.leic.daw.gomokuRoyale.domain.user.UserDomainConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours

class UserDomainTests {
    private lateinit var userDomain: UserDomain
    private lateinit var tokenEncoder: TokenEncoder
    private lateinit var config: UserDomainConfig

    @BeforeEach
    fun setup() {
        tokenEncoder = Sha256TokenEncoder()
        config = UserDomainConfig(tokenSizeInBytes = 32, tokenTtl = 1.hours, tokenRollingTtl = 1.hours, maxTokensPerUser = 1)
        userDomain = UserDomain(tokenEncoder, config)
    }

    @Test
    fun `hashPassword should return correct hash`() {
        val password = "password"
        val hashedPassword = userDomain.hashPassword(password)
        assertEquals(hashedPassword, userDomain.hashPassword(password))
    }

    @Test
    fun `checkPassword should return true for correct password`() {
        val password = "password"
        val hashedPassword = userDomain.hashPassword(password)
        assertTrue(userDomain.checkPassword(password, hashedPassword))
    }

    @Test
    fun `checkUserCredentialsRegister should not throw exception for valid credentials`() {
        assertDoesNotThrow { userDomain.checkUserCredentialsRegister("username", "email@email.com", "Password1") }
    }

    @Test
    fun `checkUserCredentialsRegister should throw exception for invalid credentials`() {
        assertThrows<UserInvalidUsername> { userDomain.checkUserCredentialsRegister("", "email@email.com", "Password1") }
        assertThrows<UserInvalidEmail> { userDomain.checkUserCredentialsRegister("username", "invalidemail", "Password1") }
        assertThrows<UserInvalidPassword> { userDomain.checkUserCredentialsRegister("username", "email@email.com", "pass") }
    }
}
