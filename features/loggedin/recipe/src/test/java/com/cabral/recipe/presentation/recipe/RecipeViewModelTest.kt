package com.cabral.recipe.presentation.recipe

import app.cash.turbine.test
import com.cabral.core.common.domain.usecase.AddRecipeUseCase
import com.cabral.test_utils.stubs.recipeStub
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
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
        coEvery { addRecipeUseCase(any()) } returns flowOf("recipe_key_document")
        //coEvery { addRecipeUseCase(match { it.name == recipeStub.name && it.volume == recipeStub.volume && it.expectedProfit == recipeStub.expectedProfit }) } returns flowOf("recipe_key_document")
        viewModel = RecipeViewModel(addRecipeUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test addRecipe triggers correct uiEvent flow`() = runTest {
        // Arrange
        viewModel.uiEvent.test {

            // Act
            viewModel.addRecipe(recipeStub.name, recipeStub.volume, recipeStub.expectedProfit)

            // Assert
            assertEquals(UiEvent.StartLoading, awaitItem())
            assertEquals(UiEvent.Success, awaitItem())
            assertEquals(UiEvent.StopLoading, awaitItem())
        }
    }

    @Test
    fun `test addRecipe handles error state`() = runTest {
        // Arrange
        coEvery { addRecipeUseCase(recipeStub) } returns flow {
            throw Exception()
        }
        viewModel = RecipeViewModel(addRecipeUseCase)

        viewModel.uiEvent.test {
            // Act
            viewModel.addRecipe("", 10f, 5f)

            // Assert
            assertEquals(UiEvent.StopLoading, awaitItem())
            assertEquals(
                UiEvent.Error("Preencha o campo nome da Receita corretamente"),
                awaitItem()
            )

        }
    }

    @Test
    fun `test addRecipe with empty name triggers error`() = runTest {
        // Arrange
        viewModel.uiEvent.test {
            // Act
            viewModel.addRecipe("", 10f, 5f)

            // Assert
            assertEquals(UiEvent.StopLoading, awaitItem())
            assertEquals(
                UiEvent.Error("Preencha o campo nome da Receita corretamente"),
                awaitItem()
            )
        }
    }
}
