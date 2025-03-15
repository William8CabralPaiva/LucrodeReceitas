package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.repository.UserRepository
import com.cabral.test_utils.stubs.userStub
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
class AutoLoginUseCaseTest {

    private val repository: UserRepository = mockk()
    private val subject = AutoLoginUseCase(repository)

    @Test
    fun `invoke Should return user successfully`() = runTest {
        // Arrange
        val key = "test_key"
        val expectedUser = userStub()
        coEvery { repository.autoLogin(key) } returns flowOf(expectedUser)

        // Act
        subject(key).collect { result ->
            // Assert
            assertEquals(expectedUser, result)
        }
        coVerify { repository.autoLogin(key) }
    }

    @Test
    fun `invoke Should return failure when repository throws an error`() = runTest {
        // Arrange
        val key = "test_key"
        val expectedResult = "Test error"
        val throwable = Throwable(expectedResult)
        coEvery { repository.autoLogin(key) } throws throwable

        // Act
        val result = assertFailsWith<Throwable> { subject(key).collect {} }

        // Assert
        assertEquals(expectedResult, result.message)
        coVerify { repository.autoLogin(key) }
    }
}