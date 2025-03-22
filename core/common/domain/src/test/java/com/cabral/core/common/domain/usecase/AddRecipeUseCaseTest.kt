package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.repository.RecipeRepository
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
class AddRecipeUseCaseTest {

    private val repository: RecipeRepository = mockk()
    private val subject = AddRecipeUseCase(repository)

    @Test
    fun `invoke Should add recipe successfully and return recipe id`() = runTest {
        // Arrange
        val recipe = recipeStub()
        val expectedResult = "recipe_id"
        coEvery { repository.addRecipe(recipe) } returns flowOf(expectedResult)

        // Act
        subject(recipe).collect { result ->
            // Assert
            assertEquals(expectedResult, result)
        }
        coVerify { repository.addRecipe(recipe) }
    }

    @Test
    fun `invoke Should return failure when repository throws an error`() = runTest {
        // Arrange
        val recipe = recipeStub()
        val expectedResult = "Test error"
        val throwable = Throwable(expectedResult)
        coEvery { repository.addRecipe(recipe) } throws throwable

        // Act
        val result = assertFailsWith<Throwable> { subject(recipe).collect {} }

        // Assert
        assertEquals(expectedResult, result.message)
        coVerify { repository.addRecipe(recipe) }
    }
}