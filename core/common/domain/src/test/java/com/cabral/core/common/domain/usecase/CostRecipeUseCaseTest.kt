package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.toRecipeCosts
import com.cabral.core.common.domain.repository.IngredientRepository
import com.cabral.test_utils.stubs.ingredientListStub
import com.cabral.test_utils.stubs.recipeStub
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class CostRecipeUseCaseTest {

    private val repository: IngredientRepository = mockk()
    private val subject = CostRecipeUseCase(repository)

    @Test
    fun `invoke Should calculate recipe costs successfully`() = runTest {
        // Arrange
        val recipe = recipeStub()
        val ingredients = ingredientListStub()
        val expectedCosts = recipe.toRecipeCosts(ingredients)
        coEvery { repository.getAllIngredients() } returns flowOf(ingredients)

        // Act
        subject(recipe).collect { result ->
            // Assert
            assertEquals(expectedCosts, result)
        }
        coVerify { repository.getAllIngredients() }
    }

    @Test
    fun `invoke Should return failure when repository throws an error during getAllIngredients`() =
        runTest {
            // Arrange
            val recipe = recipeStub()
            val expectedResult = "Test error"
            val throwable = Throwable(expectedResult)
            coEvery { repository.getAllIngredients() } throws throwable

            // Act
            val result = assertFailsWith<Throwable> { subject(recipe).collect {} }

            // Assert
            assertEquals(expectedResult, result.message)
            coVerify { repository.getAllIngredients() }
        }

}