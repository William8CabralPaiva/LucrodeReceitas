package com.cabral.recipe.presentation.listrecipe

import app.cash.turbine.test
import com.cabral.core.common.domain.usecase.DeleteRecipeUseCase
import com.cabral.core.common.domain.usecase.GetListRecipeUseCase
import com.cabral.test_utils.stubs.recipeProfitPriceStub
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
class ListRecipeViewModelTest {

    private lateinit var viewModel: ListRecipeViewModel
    private val getListRecipeUseCase: GetListRecipeUseCase = mockk(relaxed = true)
    private val deleteRecipeUseCase: DeleteRecipeUseCase = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllRecipe should emit ListRecipe when use case returns data`() = runTest {
        // Arrange
        val recipes = listOf(recipeProfitPriceStub())
        coEvery { getListRecipeUseCase() } returns flowOf(recipes)

        viewModel = ListRecipeViewModel(getListRecipeUseCase, deleteRecipeUseCase)

        // Act & Assert
        viewModel.uiState.test {
            assertEquals(UiState.StartLoading, awaitItem())
            assertEquals(UiState.ListRecipe(recipes), awaitItem())
        }
    }

    @Test
    fun `getAllRecipe should emit EmptyList when use case returns empty list`() = runTest {
        // Arrange
        coEvery { getListRecipeUseCase() } returns flowOf(emptyList())

        viewModel = ListRecipeViewModel(getListRecipeUseCase, deleteRecipeUseCase)

        // Act & Assert
        viewModel.uiState.test {
            assertEquals(UiState.StartLoading, awaitItem())
            assertEquals(UiState.EmptyList, awaitItem())
        }
    }

    @Test
    fun `getAllRecipe should emit EmptyList on error`() = runTest {
        // Arrange
        coEvery { getListRecipeUseCase() } returns flow { throw Exception("Error") }

        viewModel = ListRecipeViewModel(getListRecipeUseCase, deleteRecipeUseCase)

        // Act & Assert
        viewModel.uiState.test {
            assertEquals(UiState.StartLoading, awaitItem())
            assertEquals(UiState.EmptyList, awaitItem())
        }
    }

    @Test
    fun `deleteRecipe should emit SuccessDelete when recipe is deleted`() = runTest {
        // Arrange
        val recipe = recipeProfitPriceStub()
        coEvery { deleteRecipeUseCase(any()) } returns flowOf(Unit)

        viewModel = ListRecipeViewModel(getListRecipeUseCase, deleteRecipeUseCase)

        // Act & Assert
        viewModel.uiState.test {
            viewModel.deleteRecipe(recipe)
            assertEquals(UiState.StartLoading, awaitItem())
            assertEquals(UiState.SuccessDelete(recipe), awaitItem())
        }
    }

    @Test
    fun `deleteRecipe should emit ErrorDelete when deletion fails`() = runTest {
        // Arrange
        val recipe = recipeProfitPriceStub()
        coEvery { deleteRecipeUseCase(any()) } returns flow { throw Exception("Error") }

        viewModel = ListRecipeViewModel(getListRecipeUseCase, deleteRecipeUseCase)

        // Act & Assert
        viewModel.uiEvent.test {
            viewModel.deleteRecipe(recipe)
            recipe.name?.let {
                assertEquals(UiEvent.ErrorDelete(it), awaitItem())
            }?: assert(false)
        }
    }
}
