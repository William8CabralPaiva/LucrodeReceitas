package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.repository.IngredientRepository
import com.cabral.stubs.ingredientListStub
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
class ListIngredientUseCaseTest {

    private val repository: IngredientRepository = mockk()
    private val subject = ListIngredientUseCase(repository)

    @Test
    fun `invoke Should return ingredient list successfully`() = runTest {
        // Arrange
        val expectedIngredients = ingredientListStub()
        coEvery { repository.getAllIngredients() } returns flowOf(expectedIngredients)

        // Act
        subject().collect { result ->
            // Assert
            assertEquals(expectedIngredients, result)
        }
        coVerify { repository.getAllIngredients() }
    }

    @Test
    fun `invoke Should return failure when repository throws an error`() = runTest {
        // Arrange
        val expectedResult = "Test error"
        val throwable = Throwable(expectedResult)
        coEvery { repository.getAllIngredients() } throws throwable

        // Act
        val result = assertFailsWith<Throwable> { subject().collect {} }

        // Assert
        assertEquals(expectedResult, result.message)
        coVerify { repository.getAllIngredients() }
    }
}