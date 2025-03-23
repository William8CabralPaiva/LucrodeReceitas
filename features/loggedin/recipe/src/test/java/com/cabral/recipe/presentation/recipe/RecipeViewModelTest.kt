package com.cabral.recipe.presentation.recipe

import app.cash.turbine.test
import com.cabral.arch.extensions.RecipeThrowable
import com.cabral.core.common.domain.usecase.AddRecipeUseCase
import com.cabral.test_utils.stubs.recipeStub
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeViewModelTest {

    private lateinit var viewModel: RecipeViewModel
    private val addRecipeUseCase: AddRecipeUseCase = mockk(relaxed = true)
    private val recipeStub = recipeStub()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RecipeViewModel(addRecipeUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addRecipe should emit StartLoading, Success, and StopLoading`() = runTest {
        // Arrange
        coEvery { addRecipeUseCase(any()) } returns flowOf("recipe_key_document")

        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.addRecipe(recipeStub.name, recipeStub.volume, recipeStub.expectedProfit)

            assertEquals(UiEvent.StartLoading, awaitItem())
            assertEquals(UiEvent.Success, awaitItem())
            assertEquals(UiEvent.StopLoading, awaitItem())
        }
        assertTrue(viewModel.recipeAlreadyCreate)
        assertEquals("recipe_key_document", viewModel.recipe.keyDocument)
    }

    @Test
    fun `addRecipe should emit StopLoading and Error when name is empty`() = runTest {
        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.addRecipe("", 10f, 5f)

            assertEquals(UiEvent.StopLoading, awaitItem())
            assertEquals(
                UiEvent.Error("Preencha o campo nome da Receita corretamente"),
                awaitItem()
            )
        }
    }

    @Test
    fun `addRecipe should emit Error when use case throws exception`() = runTest {
        // Arrange
        val error = RecipeThrowable.AddRecipeThrowable()
        coEvery { addRecipeUseCase(any()) } returns flow { throw error }

        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.addRecipe(recipeStub.name, recipeStub.volume, recipeStub.expectedProfit)

            assertEquals(UiEvent.StartLoading, awaitItem())
            assertEquals(UiEvent.Error(error.message), awaitItem())
            assertEquals(UiEvent.StopLoading, awaitItem())
        }
        assertTrue(!viewModel.recipeAlreadyCreate)
    }
}
