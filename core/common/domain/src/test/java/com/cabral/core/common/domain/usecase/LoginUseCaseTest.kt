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
class LoginUseCaseTest {

    private val repository: UserRepository = mockk()
    private val subject = LoginUseCase(repository)

    @Test
    fun `invoke Should return user successfully`() = runTest {
        // Arrange
        val user = userStub()
        coEvery { repository.login(user) } returns flowOf(user)

        // Act
        subject(user).collect { result ->
            // Assert
            assertEquals(user, result)
        }
        coVerify { repository.login(user) }
    }

    @Test
    fun `invoke Should return failure when repository throws an error`() = runTest {
        // Arrange
        val user = userStub()
        val expectedResult = "Test error"
        val throwable = Throwable(expectedResult)
        coEvery { repository.login(user) } throws throwable

        // Act
        val result = assertFailsWith<Throwable> { subject(user).collect {} }

        // Assert
        assertEquals(expectedResult, result.message)
        coVerify { repository.login(user) }
    }
}