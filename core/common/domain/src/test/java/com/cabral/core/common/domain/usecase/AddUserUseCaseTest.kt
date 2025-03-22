package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.repository.UserRepository
import com.cabral.test_utils.stubs.userStub
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import app.cash.turbine.test
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class AddUserUseCaseTest {

    private val repository: UserRepository = mockk()
    private val subject = AddUserUseCase(repository)

    @Test
    fun `invoke Should add user successfully`() = runTest {
        // Arrange
        val user = userStub()
        coEvery { repository.addUser(user) } returns flowOf(Unit)

        // Act & Assert
        subject(user).test {
            // Assert
            assertEquals(Unit, awaitItem()) // Verifica se o fluxo emitiu Unit
            awaitComplete() // Verifica que o fluxo foi concluído
        }

        coVerify { repository.addUser(user) }
    }

    @Test
    fun `invoke Should return failure when repository throws an error`() = runTest {
        // Arrange
        val user = userStub()
        val expectedResult = "Test error"
        val throwable = Throwable(expectedResult)
        coEvery { repository.addUser(user) } throws throwable

        // Act
        val result = assertFailsWith<Throwable> { subject(user).collect {} }

        // Assert
        assertEquals(expectedResult, result.message)
        coVerify { repository.addUser(user) }
    }
}
