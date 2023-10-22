package pt.isel.leic.daw.gomokuRoyale.domain

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import pt.isel.leic.daw.gomokuRoyale.domain.user.User
import kotlin.test.Test

class UserTests {
    @Test
    fun `valid email should return true`() {
        assertTrue(User.validEmail("test@email.com"))
    }

    @Test
    fun `invalid email should return false`() {
        assertFalse(User.validEmail("invalidemail"))
    }

    @Test
    fun `valid name should return true`() {
        assertTrue(User.validName("testName"))
    }

    @Test
    fun `invalid name should return false`() {
        assertFalse(User.validName("t"))
    }

    @Test
    fun `valid id should return true`() {
        assertTrue(User.validId(1))
    }

    @Test
    fun `invalid id should return false`() {
        assertFalse(User.validId(-1))
    }

    @Test
    fun `calculateNewRating should return correct rating`() {
        val user = User(1, "testName", "test@email.com", "password", 1500.0)
        val newRating = user.calculateNewRating(1.0, 1400.0)
        assertTrue(newRating > 1500.0)
    }
}
