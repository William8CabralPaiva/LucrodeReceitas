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
class AddIngredientUseCaseTest {

    private val repository: IngredientRepository = mockk()
    private val subject = AddIngredientUseCase(repository)

    @Test
    fun `invoke Should add ingredient list successfully`() = runTest {
        // Arrange
        val ingredients = listOf(ingredientStub())
        coEvery { repository.addIngredient(ingredients) } returns flowOf(Unit)

        // Act
       subject(ingredients)

        // Assert
        coVerify { repository.addIngredient(ingredients) }
    }

    @Test
    fun `invoke Should return failure when repository throws an error`() = runTest {
        // Arrange
        val ingredients = listOf(ingredientStub())
        val expectedResult = "Test error"
        val throwable = Throwable(expectedResult)
        coEvery { repository.addIngredient(ingredients) } throws throwable

        // Act
        val result = assertFailsWith<Throwable> { subject(ingredients) }

        // Assert
        assertEquals(expectedResult, result.message)
        coVerify { repository.addIngredient(ingredients) }
    }
}
