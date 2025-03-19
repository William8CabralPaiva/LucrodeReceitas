package com.cabral.ingredient.presentation.ingredient

import app.cash.turbine.test
import com.cabral.core.common.domain.usecase.AddIngredientUseCase
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
class IngredientsViewModelTest {

    private lateinit var viewModel: IngredientsViewModel
    private val addIngredientUseCase: AddIngredientUseCase = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { addIngredientUseCase(any()) } returns flowOf(Unit)
        viewModel = IngredientsViewModel(addIngredientUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test save emits success event`() = runTest {
        // Arrange
        viewModel.uiEvent.test {
            // Act
            viewModel.save()

            // Assert
            assertEquals(UiEvent.Success, awaitItem())
        }
    }

    @Test
    fun `test save handles error`() = runTest {
        // Arrange
        coEvery { addIngredientUseCase(any()) } returns flow {
            throw Exception()
        }
        viewModel = IngredientsViewModel(addIngredientUseCase)

        viewModel.uiEvent.test {
            // Act
            viewModel.save()

            // Assert
            assertEquals(UiEvent.Error, awaitItem())
        }
    }

    @Test
    fun `test addOrEditIngredient with valid input adds ingredient`() = runTest {
        // Arrange
        viewModel.uiState.test {
            // Act
            viewModel.addOrEditIngredient("Sugar", "10", "g", "5")

            // Assert
            assertEquals(UiState.Default, awaitItem())
            assertEquals(UiState.SuccessAdd(0), awaitItem())
        }
    }

    @Test
    fun `test addOrEditIngredient with empty name triggers error`() = runTest {
        // Arrange
        viewModel.uiState.test {
            // Act
            viewModel.addOrEditIngredient("", "10", "g", "5")

            // Assert
            assertEquals(UiState.Default, awaitItem())
            assert(awaitItem() is UiState.ErrorAddEdit)
        }
    }
}
