package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.repository.IngredientRepository
import com.cabral.stubs.ingredientStub
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
class DeleteIngredientUseCaseTest {

    private val repository: IngredientRepository = mockk()
    private val subject = DeleteIngredientUseCase(repository)

    @Test
    fun `invoke Should delete ingredient successfully`() = runTest {
        // Arrange
        val ingredient = ingredientStub()
        coEvery { repository.deleteIngredient(ingredient) } returns flowOf(Unit)

        // Act
        subject(ingredient).collect { result ->
            // Assert
            assertEquals(Unit, result)
        }
        coVerify { repository.deleteIngredient(ingredient) }
    }

    @Test
    fun `invoke Should return failure when repository throws an error`() = runTest {
        // Arrange
        val ingredient = ingredientStub()
        val expectedResult = "Test error"
        val throwable = Throwable(expectedResult)
        coEvery { repository.deleteIngredient(ingredient) } throws throwable

        // Act
        val result = assertFailsWith<Throwable> { subject(ingredient).collect {} }

        // Assert
        assertEquals(expectedResult, result.message)
        coVerify { repository.deleteIngredient(ingredient) }
    }
}