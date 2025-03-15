package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.repository.RecipeRepository
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
class DeleteRecipeUseCaseTest {

    private val repository: RecipeRepository = mockk()
    private val subject = DeleteRecipeUseCase(repository)

    @Test
    fun `invoke Should delete recipe successfully`() = runTest {
        // Arrange
        val keyDocument = "recipe_key"
        coEvery { repository.deleteRecipe(keyDocument) } returns flowOf(Unit)

        // Act
        subject(keyDocument).collect { result ->
            // Assert
            assertEquals(Unit, result)
        }
        coVerify { repository.deleteRecipe(keyDocument) }
    }

    @Test
    fun `invoke Should return failure when repository throws an error`() = runTest {
        // Arrange
        val keyDocument = "recipe_key"
        val expectedResult = "Test error"
        val throwable = Throwable(expectedResult)
        coEvery { repository.deleteRecipe(keyDocument) } throws throwable

        // Act
        val result = assertFailsWith<Throwable> { subject(keyDocument).collect {} }

        // Assert
        assertEquals(expectedResult, result.message)
        coVerify { repository.deleteRecipe(keyDocument) }
    }
}