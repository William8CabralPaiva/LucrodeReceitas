package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.repository.UserRepository
import com.cabral.stubs.userStub
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class GoogleLoginUseCaseTest {

    private val repository: UserRepository = mockk()
    private val subject = GoogleLoginUseCase(repository)

    @Test
    fun `invoke Should return user successfully`() = runTest {
        // Arrange
        val email = "test@example.com"
        val name = "Test User"
        val expectedUser = userStub()
        coEvery { repository.googleLogin(email, name) } returns flowOf(expectedUser)

        // Act
        subject(email, name).collect { result ->
            // Assert
            assertEquals(expectedUser, result)
        }
        coVerify { repository.googleLogin(email, name) }
    }

    @Test
    fun `invoke Should return failure when repository throws an error`() = runTest {
        // Arrange
        val email = "test@example.com"
        val name = "Test User"
        val expectedResult = "Test error"
        val throwable = Throwable(expectedResult)
        coEvery { repository.googleLogin(email, name) } throws throwable

        // Act
        val result = assertFailsWith<Throwable> { subject(email, name).collect {} }

        // Assert
        assertEquals(expectedResult, result.message)
        coVerify { repository.googleLogin(email, name) }
    }
}