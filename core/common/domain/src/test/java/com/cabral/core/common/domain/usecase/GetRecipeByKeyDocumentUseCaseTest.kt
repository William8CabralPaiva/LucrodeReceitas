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
class GetRecipeByKeyDocumentUseCaseTest {

    private val repository: RecipeRepository = mockk()
    private val subject = GetRecipeByKeyDocumentUseCase(repository)

    @Test
    fun `invoke Should return recipe successfully`() = runTest {
        // Arrange
        val keyDocument = "test-key"
        val expectedRecipe = recipeStub()
        coEvery { repository.getRecipeByKeyDocument(keyDocument) } returns flowOf(expectedRecipe)

        // Act
        subject(keyDocument).collect { result ->
            // Assert
            assertEquals(expectedRecipe, result)
        }
        coVerify { repository.getRecipeByKeyDocument(keyDocument) }
    }

    @Test
    fun `invoke Should return failure when repository throws an error`() = runTest {
        // Arrange
        val keyDocument = "test-key"
        val expectedResult = "Test error"
        val throwable = Throwable(expectedResult)
        coEvery { repository.getRecipeByKeyDocument(keyDocument) } throws throwable

        // Act
        val result = assertFailsWith<Throwable> { subject(keyDocument).collect {} }

        // Assert
        assertEquals(expectedResult, result.message)
        coVerify { repository.getRecipeByKeyDocument(keyDocument) }
    }
}
