package com.cabral.recipe.presentation.listrecipe

import com.cabral.core.common.domain.usecase.DeleteRecipeUseCase
import com.cabral.core.common.domain.usecase.GetListRecipeUseCase
import com.cabral.test_utils.stubs.recipeProfitPriceListStub
import com.cabral.test_utils.stubs.recipeProfitPriceStub
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
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
class ListRecipeViewModelTest {

    private val getListRecipeUseCase: GetListRecipeUseCase = mockk(relaxed = true)
    private val deleteRecipeUseCase: DeleteRecipeUseCase = mockk(relaxed = true)
    private lateinit var viewModel: ListRecipeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ListRecipeViewModel(getListRecipeUseCase, deleteRecipeUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllRecipe with recipes should update listRecipe and emit ListRecipe`() = runTest {
        // Arrange
        val recipes = recipeProfitPriceListStub()
        coEvery { getListRecipeUseCase() } returns flowOf(recipes)

        val states = mutableListOf<UiState>()
        val collectJob = launch {
            viewModel.uiState.collect { states.add(it) }
        }

        // Act
        viewModel.getAllRecipe()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(recipes, viewModel.listRecipe)
        assertTrue(states.any { it is UiState.ListRecipe })
        collectJob.cancel()
    }

    @Test
    fun `getAllRecipe with empty list should emit EmptyList`() = runTest {
        // Arrange
        coEvery { getListRecipeUseCase() } returns flowOf(emptyList())

        val states = mutableListOf<UiState>()
        val collectJob = launch {
            viewModel.uiState.collect { states.add(it) }
        }

        // Act
        viewModel.getAllRecipe()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertTrue(states.any { it is UiState.EmptyList })
        collectJob.cancel()
    }

    @Test
    fun `getAllRecipe with error should emit EmptyList`() = runTest {
        // Arrange
        coEvery { getListRecipeUseCase() } returns flow { throw Exception("Error") }

        val states = mutableListOf<UiState>()
        val collectJob = launch {
            viewModel.uiState.collect { states.add(it) }
        }

        // Act
        viewModel.getAllRecipe()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertTrue(states.any { it is UiState.EmptyList })
        collectJob.cancel()
    }

    @Test
    fun `deleteRecipe with valid key should emit SuccessDelete`() = runTest {
        // Arrange
        val recipe = recipeProfitPriceStub()
        coEvery { recipe.keyDocument?.let { deleteRecipeUseCase(it) } } returns flowOf(Unit)

        val states = mutableListOf<UiState>()
        val collectJob = launch {
            viewModel.uiState.collect { states.add(it) }
        }

        // Act
        viewModel.deleteRecipe(recipe)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertTrue(states.any { it is UiState.SuccessDelete })
        collectJob.cancel()
    }

    @Test
    fun `deleteRecipe with error should emit ErrorDelete`() = runTest {
        // Arrange
        val recipe = recipeProfitPriceStub()
        coEvery { recipe.keyDocument?.let { deleteRecipeUseCase(it) } } returns flow {
            throw Exception(
                "Error"
            )
        }

        val events = mutableListOf<UiEvent>()
        val collectJob = launch {
            viewModel.uiEvent.collect { events.add(it) }
        }

        // Act
        viewModel.deleteRecipe(recipe)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertTrue(events.any { it is UiEvent.ErrorDelete })
        collectJob.cancel()
    }

    @Test
    fun `getAllRecipe should emit StartLoading initially`() = runTest {
        // Arrange
        val states = mutableListOf<UiState>()
        val collectJob = launch {
            viewModel.uiState.collect { states.add(it) }
        }

        // Act
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        assertEquals(UiState.StartLoading, states.first())
        collectJob.cancel()
    }
}