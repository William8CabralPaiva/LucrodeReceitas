package com.cabral.hubsrc.repository

import com.cabral.core.common.domain.model.User
import com.cabral.remote.local.RemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class UserRepositoryImplTest {

    private val remoteDataSource: RemoteDataSource = mockk()
    private lateinit var userRepository: UserRepositoryImpl

    @Before
    fun setup() {
        userRepository = UserRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `addUser should return unit`() = runTest {
        // Arrange
        val user = User("1", "John Doe", "johndoe@example.com")
        coEvery { remoteDataSource.addUser(user) } returns flowOf(Unit)

        // Act
        val result = userRepository.addUser(user)

        // Assert
        result.collect { response ->
            assertEquals(Unit, response)
        }
    }

    @Test
    fun `login should return User`() = runTest {
        // Arrange
        val user = User("1", "John Doe", "johndoe@example.com")
        coEvery { remoteDataSource.login(user) } returns flowOf(user)

        // Act
        val result = userRepository.login(user)

        // Assert
        result.collect { loggedInUser ->
            assertEquals(user, loggedInUser)
        }
    }

    @Test
    fun `autoLogin should return User`() = runTest {
        // Arrange
        val user = User("1", "John Doe", "johndoe@example.com")
        coEvery { remoteDataSource.autoLogin("key123") } returns flowOf(user)

        // Act
        val result = userRepository.autoLogin("key123")

        // Assert
        result.collect { autoLoggedInUser ->
            assertEquals(user, autoLoggedInUser)
        }
    }

    @Test
    fun `googleLogin should return User`() = runTest {
        // Arrange
        val user = User("1", "John Doe", "johndoe@example.com")
        coEvery { remoteDataSource.googleLogin("johndoe@example.com", "John Doe") } returns flowOf(user)

        // Act
        val result = userRepository.googleLogin("johndoe@example.com", "John Doe")

        // Assert
        result.collect { googleLoggedInUser ->
            assertEquals(user, googleLoggedInUser)
        }
    }

    @Test
    fun `forgotPassword should call remoteDataSource with email`() = runTest {
        // Arrange
        val email = "johndoe@example.com"
        coEvery { remoteDataSource.forgotPassword(email) } returns Unit

        // Act
        userRepository.forgotPassword(email)

        // Assert
        coVerify { remoteDataSource.forgotPassword(email) }
    }
}
