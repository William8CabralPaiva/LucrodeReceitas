package com.cabral.ingredient.presentation.listingredient

import app.cash.turbine.test
import com.cabral.arch.extensions.IngredientThrowable
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.usecase.DeleteIngredientUseCase
import com.cabral.core.common.domain.usecase.ListIngredientUseCase
import com.cabral.test_utils.stubs.ingredientListStub
import com.cabral.test_utils.stubs.ingredientStub
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListIngredientsViewModelTest {

    private lateinit var viewModel: ListIngredientsViewModel
    private val listIngredientUseCase: ListIngredientUseCase = mockk()
    private val deleteIngredientUseCase: DeleteIngredientUseCase = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ListIngredientsViewModel(listIngredientUseCase, deleteIngredientUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getAllIngredients returns list`() = runTest {
        // Arrange
        val ingredientList = ingredientListStub()
        coEvery { listIngredientUseCase() } returns flowOf(ingredientList)

        // Act
        viewModel.getAllIngredients()

        // Assert
        viewModel.uiState.test {
            assertEquals(UiState.StartLoading, awaitItem())
            assertEquals(UiState.ListIngredient(ingredientList), awaitItem())
        }
    }

    @Test
    fun `test getAllIngredients returns empty list`() = runTest {
        // Arrange
        coEvery { listIngredientUseCase() } returns flowOf(emptyList())

        // Act
        viewModel.getAllIngredients()

        // Assert
        viewModel.uiState.test {
            assertEquals(UiState.StartLoading, awaitItem())
            assertEquals(UiState.EmptyList, awaitItem())
        }
    }

    @Test
    fun `test getAllIngredients handles error`() = runTest {
        // Arrange
        coEvery { listIngredientUseCase() } returns flow { IngredientThrowable.PriceThrowable() }

        // Act
        viewModel.getAllIngredients()

        // Assert
        viewModel.uiState.test {
            assertEquals(UiState.StartLoading, awaitItem())
            assertEquals(UiState.EmptyList, awaitItem())
        }
    }

    @Test
    fun `test deleteIngredient triggers success event`() = runTest {
        // Arrange
        val ingredient = ingredientStub()
        coEvery { deleteIngredientUseCase(ingredient) } returns flowOf(Unit)

        // Act
        viewModel.deleteIngredient(ingredient)

        // Assert
        viewModel.uiEvent.test {
            assertEquals(UiEvent.SuccessRemoveIngredient(ingredient), awaitItem())
        }
    }

    @Test
    fun `test deleteIngredient triggers error event`() = runTest {
        // Arrange
        val ingredient = Ingredient(1, "Salt", 1f, "kg", 5f, "key2")
        coEvery { deleteIngredientUseCase(ingredient) } returns flow { IngredientThrowable.NameThrowable() }

        // Act
        viewModel.deleteIngredient(ingredient)

        // Assert
        viewModel.uiEvent.test {
            assertEquals(UiEvent.ErrorRemoveIngredient(ingredient.name!!), awaitItem())
        }
    }
}