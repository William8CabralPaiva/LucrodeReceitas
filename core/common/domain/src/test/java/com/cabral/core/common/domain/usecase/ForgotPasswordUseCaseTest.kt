package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class ForgotPasswordUseCaseTest {

    private val repository: UserRepository = mockk()
    private val subject = ForgotPasswordUseCase(repository)

    @Test
    fun `invoke Should call forgotPassword successfully`() = runTest {
        // Arrange
        val email = "test@example.com"
        coEvery { repository.forgotPassword(email) } returns Unit

        // Act
        subject(email)

        // Assert
        coVerify { repository.forgotPassword(email) }
    }

    @Test
    fun `invoke Should return failure when repository throws an error`() = runTest {
        // Arrange
        val email = "test@example.com"
        val expectedResult = "Test error"
        val throwable = Throwable(expectedResult)
        coEvery { repository.forgotPassword(email) } throws throwable

        // Act
        val result = assertFailsWith<Throwable> { subject(email) }

        // Assert
        assertEquals(expectedResult, result.message)
        coVerify { repository.forgotPassword(email) }
    }
}